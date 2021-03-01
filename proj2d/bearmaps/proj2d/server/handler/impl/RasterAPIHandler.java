package bearmaps.proj2d.server.handler.impl;

import bearmaps.proj2d.AugmentedStreetMapGraph;
import bearmaps.proj2d.server.handler.APIRouteHandler;
import bearmaps.proj2d.utils.Tuple;
import edu.princeton.cs.algs4.In;
import spark.Request;
import spark.Response;
import bearmaps.proj2d.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2d.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 *
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     *  is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *                      the user viewport width and height.
     * @param response      : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     * can also be interpreted as the length of the numbers in the image
     * string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        double lrlon = requestParams.get("lrlon");
        double ullon = requestParams.get("ullon");
        double lrlat = requestParams.get("lrlat");
        double ullat = requestParams.get("ullat");
        double w = requestParams.get("w");
        double h = requestParams.get("h");
        if (!checkBoundingBox(lrlon, lrlat, ullon, ullat)) {
            results.put("query_success", false);
            return results;
        }
        double lonDPP = calLonDPP(lrlon, ullon, w);
        int zoomTimes = getZoomTimes(lonDPP);
        Tuple<Integer, Integer> lrImageIndex = getImageIndex(lrlon, lrlat, zoomTimes);
        Tuple<Integer, Integer> ulImageIndex = getImageIndex(ullon, ullat, zoomTimes);
        String[][] displayImages = getDisplayImages(lrImageIndex, ulImageIndex, zoomTimes);
        int divisionNum = (int) Math.pow(2, zoomTimes);
        double boundingBoxULLon = getLonByIndex(ulImageIndex.getFirst(), divisionNum);
        double boundingBoxULLat = getLatByIndex(ulImageIndex.getSecond(), divisionNum);
        double boundingBoxLRLon = getLonByIndex(lrImageIndex.getFirst() + 1, divisionNum);
        double boundingBoxLRLat = getLatByIndex(lrImageIndex.getSecond() + 1, divisionNum);
        results.put("render_grid", displayImages);
        results.put("raster_ul_lon", boundingBoxULLon);
        results.put("raster_ul_lat", boundingBoxULLat);
        results.put("raster_lr_lon", boundingBoxLRLon);
        results.put("raster_lr_lat", boundingBoxLRLat);
        results.put("depth", zoomTimes);
        results.put("query_success", true);
        System.out.println(results);
        return results;
    }
    private boolean checkBoundingBox(double lrlon, double lrlat, double ullon, double ullat) {
        if (lrlat > ullat) {
            return false;
        }
        if (lrlon < ullon) {
            return false;
        }
        return inMap(lrlon, lrlat) | inMap(ullon, ullat);
    }

    private boolean inMap(double lon, double lat) {
        return (lon < ROOT_LRLON && lon > ROOT_ULLON && lat > ROOT_LRLAT && lat < ROOT_ULLAT);
    }

    private double calLonDPP(double lrlon, double ullon, double w) {
        return (lrlon - ullon) / w;
    }

    private int getZoomTimes(double lonDPP) {
        double d0LonDPP = calLonDPP(Constants.ROOT_LRLON, Constants.ROOT_ULLON, Constants.TILE_SIZE);
        int zoomTimes = 0;
        while (d0LonDPP > lonDPP) {
            d0LonDPP = d0LonDPP / 2.;
            zoomTimes += 1;
        }
        if (zoomTimes >= 7) {
            zoomTimes = 7;
        }
        return zoomTimes;
    }

    private String[][] getDisplayImages(Tuple<Integer, Integer> lrImageIndex, Tuple<Integer, Integer> ulImageIndex, int zoomTimes) {
        int lonImageNum = lrImageIndex.getFirst() - ulImageIndex.getFirst() + 1;
        int latImageNum = lrImageIndex.getSecond() - ulImageIndex.getSecond() + 1;
        String[][] retStringArray = new String[latImageNum][lonImageNum];
        for (int i = 0; i < latImageNum; i += 1) {
            for (int j = 0; j < lonImageNum; j += 1) {
                int currentLonIndex = ulImageIndex.getFirst() + j;
                int currentLatIndex = ulImageIndex.getSecond() +  i;
                retStringArray[i][j] = "d" + zoomTimes + "_x" + currentLonIndex + "_y" + currentLatIndex + ".png";
            }
        }
        return retStringArray;
    }

    private Tuple<Integer, Integer> getImageIndex(double lon, double lat, int zoomTimes) {
        int divideNum = (int) Math.pow(2, zoomTimes);
        int lonIndex = getIndexByLon(lon, divideNum);
        int latIndex = getIndexByLat(lat, divideNum);
        return new Tuple<>(lonIndex, latIndex);
    }

    private int getIndexByLon(double lon, int divideNum) {
        return lonDichotomy(lon, divideNum);
    }

    private int getIndexByLat(double lat, int divideNum) {
        return latDichotomy(lat, divideNum);
    }

    private int lonDichotomy(double coor, int divideNum) {
        int midIndex = divideNum / 2;
        int currentUpperIndex = divideNum;
        int currentLowerIndex = 0;
        while (currentUpperIndex - currentLowerIndex > 1) {
            double midCoor = getLonByIndex(midIndex, divideNum);
            double midRightCoor = getLonByIndex(midIndex + 1, divideNum);
            double midLeftCoor = getLonByIndex(midIndex - 1, divideNum);
            if (coor > midCoor) {
                if (coor < midRightCoor) {
                    return midIndex;
                } else {
                    currentLowerIndex = midIndex;
                    midIndex = currentLowerIndex + (currentUpperIndex - currentLowerIndex)/ 2;
                }
            } else if (coor < midCoor) {
                if (coor > midLeftCoor) {
                    return midIndex - 1;
                } else {
                    currentUpperIndex = midIndex;
                    midIndex = currentLowerIndex + (currentUpperIndex - currentLowerIndex)/ 2;
                }
            }
        }
        return midIndex;
    }

    private int latDichotomy(double coor, int divideNum) {
        int currentUpperIndex = 0;
        int currentLowerIndex = divideNum;
        int midIndex = (currentLowerIndex - currentUpperIndex) / 2;
        while (currentLowerIndex - currentUpperIndex > 1) {
            double midCoor = getLatByIndex(midIndex, divideNum);
            double midUpCoor = getLatByIndex(midIndex - 1, divideNum);
            double midDownCoor = getLatByIndex(midIndex + 1, divideNum);
            if (coor > midCoor) {
                if (coor < midUpCoor) {
                    return midIndex - 1;
                } else  {
                    currentLowerIndex = midIndex;
                    midIndex = currentUpperIndex + (currentLowerIndex - currentUpperIndex) / 2;
                }
            } else if (coor < midCoor) {
                if (coor > midDownCoor) {
                    return midIndex;
                } else {
                    currentUpperIndex = midIndex;
                    midIndex = currentUpperIndex + (currentLowerIndex - currentUpperIndex);
                }
            }
        }
        return midIndex;
    }

    private double getLonByIndex(double index, int divideNum) {
        double coorCoveragePerDivision = (ROOT_LRLON - ROOT_ULLON) / divideNum;
        return coorCoveragePerDivision * index + ROOT_ULLON;
    }

    private double getLatByIndex(double index,  int divideNum) {
        double coorCoveragePerDivision = (ROOT_LRLAT - ROOT_ULLAT) / divideNum;
        return coorCoveragePerDivision * index + ROOT_ULLAT;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     *
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                           ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
