package com.uppu.swathi_giftregistryproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateProductsFile {
    public static FileWriter generateCsvFile(File fileName, StringBuffer fileContent) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(fileName);
            writer.append(fileContent);
            writer.append("\n");
            writer.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            try {
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return writer;

    }
}
