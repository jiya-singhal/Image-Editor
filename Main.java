import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class Utilities{
    static BufferedImage readImage(String path) throws IOException{
        return ImageIO.read(new File(path));
    }

    static void saveImage(BufferedImage inp, String path, String format) throws IOException{
        File outFile = new File(path);
        ImageIO.write(inp, format, outFile);
    }
}

class Editor {
    static BufferedImage greyScale(BufferedImage inp){
        int height = inp.getHeight();
        int width = inp.getWidth();

        BufferedImage out = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                out.setRGB(x, y, inp.getRGB(x, y));
            }
        }
        return out;
    }

    static BufferedImage hozFlip(BufferedImage inp){
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(width, height,inp.getType());
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int ogPixel = inp.getRGB(x, y);
                int invX = width-x-1;
                out.setRGB(invX, y, ogPixel);
            }
        }        return out;
    }

    static BufferedImage verFlip(BufferedImage inp){
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(width, height, inp.getType());
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int ogPixel = inp.getRGB(x, y);
                int invY = height-y-1;
                out.setRGB(x, invY, ogPixel);
            }
        }
        return out;
    }

    static BufferedImage rotate90CW(BufferedImage inp){
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(height, width, inp.getType());
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int pixel = inp.getRGB(x, y);
                out.setRGB(y, x, pixel);
            }
        }
        out = hozFlip(out);
        return out;
    }

    static BufferedImage rotate90ACW(BufferedImage inp){
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(height, width, inp.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = inp.getRGB(x, y);
                out.setRGB(y, x, pixel);
            }
        }

        return out;
    }

    static BufferedImage gaussianBlur(BufferedImage inp, int radius) {
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sumRed = 0, sumGreen = 0, sumBlue = 0, pixels = 0;

                // Iterate through the pixels within the specified radius around the current pixel
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int newX = Math.min(Math.max(x + dx, 0), width - 1);
                        int newY = Math.min(Math.max(y + dy, 0), height - 1);

                        // Get the RGB values of the pixel within the radius
                        Color pixel = new Color(inp.getRGB(newX, newY));
                        sumRed += pixel.getRed();
                        sumGreen += pixel.getGreen();
                        sumBlue += pixel.getBlue();
                        pixels++;
                    }
                }

                // Calculate the average color values within the radius
                int avgRed = sumRed / pixels;
                int avgGreen = sumGreen / pixels;
                int avgBlue = sumBlue / pixels;
                Color blurredPixel = new Color(avgRed, avgGreen, avgBlue);
                out.setRGB(x, y, blurredPixel.getRGB());
            }
        }
        return out;
    }
    static BufferedImage changeBrightness(BufferedImage inp, int factor){
        int height = inp.getHeight();
        int width = inp.getWidth();
        BufferedImage out = new BufferedImage(width, height, inp.getType());
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                Color pixel = new Color(inp.getRGB(x, y));

                int red = pixel.getRed();
                int green = pixel.getGreen();
                int blue = pixel.getBlue();

                int nRed = Math.min(255, Math.max(0,(red + (red*factor)/100)));
                int nBlue = Math.min(255, Math.max(0,(blue + (blue*factor)/100)));
                int nGreen = Math.min(255, Math.max(0,(green + (green*factor)/100)));

                Color newPix = new Color(nRed, nGreen, nBlue);

                out.setRGB(x, y, newPix.getRGB());
            }
        }
        return out;
    }
}

public class Main {
    public static void main(String[] args) throws IOException{
        System.out.println("Welcome to my Image Editor!!");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the file path of your input image:");
        String path = br.readLine();
        BufferedImage inp = Utilities.readImage(path);
        System.out.println("Which operation would you like to perform?");
        System.out.println("1. Convert to  GreyScale");
        System.out.println("2. Adjust the Brightness of the image");
        System.out.println("3. Rotate the image 90 Degrees Clockwise");
        System.out.println("4. Flip the image horizontally");
        System.out.println("5. Flip the image vertically");
        System.out.println("6. Blur the image");
        System.out.println("7. Rotate the image 90 Degrees Anti Clockwise");
        BufferedImage out = null;
        int option = Integer.parseInt(br.readLine());
        boolean edited = true;
        switch (option) {
            case 1 :
                out = Editor.greyScale(inp);
                break;
            case 2 :
                System.out.println("Enter the brightness adjustment factor:");
                System.out.println("To increase brightness, enter a positive value.");
                System.out.println("To decrease brightness, enter a negative value.");
                int factor = Integer.parseInt(br.readLine());
                out = Editor.changeBrightness(inp, factor);
                break;
            case 3 :
                out = Editor.rotate90CW(inp);
                break;
            case 4 :
                out = Editor.hozFlip(inp);
                break;
            case 5 :
                out = Editor.verFlip(inp);
                break;
            case 6 :
                System.out.println("Enter the strength of blur");
                int radius = Integer.parseInt(br.readLine());
                out = Editor.gaussianBlur(inp, radius);
                break;
            case 7 :
                    out = Editor.rotate90ACW(inp);
                    break;
            default :
                System.out.println("Invalid option selected!");
                edited = false;
        }

        if (edited) {
            System.out.println("Please specify the file path where you want to save the edited image:");
            String outputPath = br.readLine();
            System.out.println("Enter the desired image format (e.g., PNG, JPEG):");
            String outputFormat = br.readLine();

            try {
                Utilities.saveImage(out, outputPath, outputFormat);
                System.out.println("The edited image has been successfully saved at: " + outputPath);
            } catch (IOException e) {
                System.err.println("An error occurred while saving the edited image: " + e.getMessage());
            }
        }
    }
}