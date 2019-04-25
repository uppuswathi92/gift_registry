package com.uppu.swathi_giftregistryproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateProductsFile {
    //generates a csv file based on the file name and file content received
    public static FileWriter generateCsvFile(File fileName, StringBuffer fileContent) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            writer.append(fileContent);
            writer.append("\n");
            //makes the buffer empty once content has been written
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            try {
                writer.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return writer;

    }
}
