import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * Program to hide secret messages in images. This program takes in an image and outputs in image altered in a very
 * slight way which is pretty unnoticeable. It also generates a key in a text file which contains the coordinates of
 * the pixels that were changed. To properly decode a message the original image, the altered image and the key.
 *
 * @author Michael Smith
 */
public class Encrypter {

    BufferedImage inputImage;
    BufferedImage keyImage;
    BufferedImage outputImage;

    public Encrypter() {

    }

    public BufferedImage encryptMessage(String message){
        BufferedImage output = copyBufferedImage(inputImage);

        int length = inputImage.getWidth()*inputImage.getHeight();
        int[] xcoords = new int[message.length()*8];
        int[] ycoords = new int[message.length()*8];
        String bits = "";

        String[] bytes = new String[message.length()];
        String temp;
        for(int i = 0; i < message.length(); i++) {
            bytes[i] = Integer.toBinaryString(message.charAt(i));
            temp = Integer.toBinaryString(message.charAt(i));
            if(temp.length() != 8) {
                for(int j = 0; j < 9 - temp.length(); j++) {
                    temp = "0" + temp;
                }
            }
            bits = bits + temp;
        }

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");

        }
        System.out.print("\n" + bits + "\n");

        for(int i = 0; i < message.length()*8; i++) {
            xcoords[i] = (int) (Math.random()*inputImage.getWidth());
            ycoords[i] = (int) (Math.random()*inputImage.getHeight());
        }

        try {
            FileWriter writer = new FileWriter("key.txt");
            for(int i = 0; i < bits.length(); i++){
                if(bits.charAt(i) == ' ') continue;
                if(bits.charAt(i) == '1') {
                    System.out.printf("changing %d, %d from %d to %d\n", xcoords[i], ycoords[i], inputImage.getRGB(xcoords[i], ycoords[i]), inputImage.getRGB(xcoords[i], ycoords[i]) - 1);
                    output.setRGB(xcoords[i], ycoords[i], inputImage.getRGB(xcoords[i], ycoords[i]) - 1);
                    writer.write(xcoords[i] + "\n" + ycoords[i] + "\n");
                    writer.flush();
                } else {
                    System.out.printf("changing %d, %d from %d to %d\n", xcoords[i], ycoords[i], inputImage.getRGB(xcoords[i], ycoords[i]), inputImage.getRGB(xcoords[i], ycoords[i]) + 1);
                    output.setRGB(xcoords[i], ycoords[i], inputImage.getRGB(xcoords[i], ycoords[i]) + 1);
                    writer.write(xcoords[i] + "\n" + ycoords[i] + "\n");
                    writer.flush();
                }


            }
            writer.close();
        } catch (Exception e) {

        }

        return output;

    }

    public String decrypt(BufferedImage input) {
        String outputMessage = "";

        int lines = 0;
        try{
            BufferedReader lineCounter = new BufferedReader(new FileReader("key.txt"));
            while (lineCounter.readLine() != null) lines++;
            lineCounter.close();
        } catch (Exception e) {

        }


        try {
            BufferedReader reader = new BufferedReader(new FileReader("key.txt"));
            int x;
            int y;
            System.out.println((lines)/2);
            for(int i = 1; i < (lines/2)+1; i++) {

                x = Integer.parseInt(reader.readLine());
                y = Integer.parseInt(reader.readLine());

                //System.out.println(x + ", " + y);
                //System.out.println(inputImage.getRGB(x, y) + ", " + input.getRGB(x, y));

                if(inputImage.getRGB(x, y) == input.getRGB(x, y)) {
                    outputMessage = outputMessage + "1";
                } else {
                    outputMessage = outputMessage + "0";
                }


            }
            reader.close();
        } catch (Exception e) {
            System.out.print("reader error");
        }
        String temp = outputMessage;
        outputMessage = "";
        String parse;
        int parseInt;
        char c;

        while(temp.length() > 1) {
            System.out.println("temp: " + temp);
            parse = temp.substring(0, 8);
            System.out.println("parse: " + parse);
            parseInt = Integer.parseInt(parse, 2);
            c = (char)parseInt;
            outputMessage = outputMessage + c;
            temp = temp.substring(8);

        }


        return outputMessage;
    }

    public static void main(String[] args) {

        Encrypter e1 = new Encrypter();
        try {
            e1.inputImage = ImageIO.read(new File("src/testImage.png"));
        } catch (Exception e) {
//LoremipsumdolorsitametconsecteturadipiscingelitseddoeiusmodtemporincididuntutlaboreetdoloremagnaaliquaJustoegetmagnafermentumiaculiseunondiamPulvinaretiamnonquamlacussuspendissefaucibusinterdumposuereloremAmetconsecteturadipiscingelitduistristiquesollicitudinnibhsitametArcucursuseuismodquisviverranibhQuisauctorelitsedvulputatemiUrnamolestieatelementumeufacilisissedEumibibendumnequeegestasconguequisqueegestasdiaminAeneansedadipiscingdiamdonecadipiscingMassasapienfaucibusetmolestieacfeugiatsedlectusvestibulumElementumcurabiturvitaenuncsedvelitIdvolutpatlacuslaoreetnoncurabiturgravidaarcuactortorTelluspellentesqueeutincidunttortoraliquamnullafacilisicrasDignissimcrastinciduntlobortisfeugiatvivamusataugueAcauctorauguemaurisauguenequegravidainfermentumetHendreritdolormagnaegetestloremipsumdolorsitInnislnisiscelerisqueeuultricesvitaeauctoreuaugueDuiutornarelectussitMaecenaspharetraconvallisposueremorbileoAcfelisdonecetodiopellentesqueJustoegetmagnafermentumiaculiseuNibhnislcondimentumidvenenatisEuturpisegestaspretiumaeneanpharetramagnaacplaceratAmetrisusnullamegetfelisegetLaoreetnoncurabiturgravidaarcuNisilacussedviverratellusinhacMaecenaspharetraconvallisposueremorbileournamolestieatEgetmagnafermentumiaculiseuSedvulputateodioutenimblanditvolutpatmaecenasvolutpatblanditNecnamaliquamsemetViverranibhcraspulvinarmattisnuncsedEtnetusetmalesuadafamesAmetcursussitametdictumsitametjustodonecUllamcorpersitametrisusnullamFacilisimorbitempusiaculisurnaidvolutpatEufacilisissedodiomorbiquiscommodoPulvinarpellentesquehabitantmorbitristiquesenectusetnetusetmalesuadaPurusutfaucibuspulvinarelementumintegerenimnequevolutpatVolutpatmaecenasvolutpatblanditaliquametiameratArcuactortordignissimconvallisCommodosedegestasegestasfringillaphasellusfaucibusscelerisqueeleifenddonecRhoncusdolorpurusnonenimpraesentelementumfacilisisleoDonecetodiopellentesquediamvolutpatcommodosedEgestasintegeregetaliquetnibhpraesenttristiquemagnaFeugiatinantemetusdictumattemporNetusetmalesuadafamesacturpisegestasmaecenasElementumsagittisvitaeetleoduisutdiamquamOrciacauctorauguemaurisauguenequegravidainMassamassaultriciesmiquishendreritdolormagnaegetAliquamvestibulummorbiblanditcursusrisusUrnaetpharetrapharetramassamassaultriciesmiCursusvitaeconguemaurisrhoncusAliquetnibhpraesenttristiquemagnasitOrciascelerisquepurussemperInmetusvulputateeuscelerisquefelisTristiquemagnasitametpurusgravidaPellentesqueelitullamcorperdignissimcrastinciduntPortanonpulvinarnequelaoreetsuspendisseinterdumconsecteturHabitantmorbitristiquesenectusetnetusArcunonodioeuismodlaciniaatquisrisussedSemperrisusinhendreritgravidarutrumquisqueFeugiatinfermentumposuereurnanectinciduntActortordignissimconvallisaeneanettortorat
        }
        e1.outputImage = e1.encryptMessage("Mikey is a weirdo \uD83D\uDE01");


        try {
            File testOutput = new File("testOutput.png");
            BufferedImage output = e1.outputImage;
            ImageIO.write(output, "png", testOutput);
        } catch (Exception e) {
            System.out.println("no file");
        }

        System.out.println(e1.decrypt(e1.outputImage));

    }

    static BufferedImage copyBufferedImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
