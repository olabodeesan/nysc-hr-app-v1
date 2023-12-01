/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.fileupload;

import app.exceptions.AppException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import javax.sql.DataSource;
import org.primefaces.model.UploadedFile;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class FileUploadService implements Serializable {

    private ServiceUtil serviceUtil;
    private DBUtil dbUtil;

    public FileUploadService() {
        serviceUtil = new ServiceUtil();
        dbUtil = new DBUtil();
    }

    public boolean uploadPersonnelPhoto(UploadedFile uploaded_file, String employeeRecId)
            throws AppException {

        boolean result = false;

        StringBuilder strBuilder = null;

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        boolean _file_uploaded = false;

        try {

            //check if staff folder exists else create it
            ViewHelper viewHelper = new ViewHelper();
            strBuilder = new StringBuilder();

            strBuilder.append(viewHelper.getPersonnelDocsDir()).append("/").append(employeeRecId);
            String employeeDirName = strBuilder.toString();

            File employeeDir = new File(employeeDirName);
            boolean dir_exists = employeeDir.exists();

            if (!dir_exists) {
                employeeDir.mkdir();
            }

            //test again if it exists
            dir_exists = employeeDir.exists();

            if (dir_exists) {
                //get full file name
                strBuilder = new StringBuilder();
                strBuilder.append(employeeDirName).append("/").append(employeeRecId).append(".jpg");
                String filePath = strBuilder.toString();

                //upload the file to disk
                File content_file = new File(filePath);
                fileOutputStream = new FileOutputStream(content_file);
                byte[] buffer = new byte[6124];
                int bulk;
                inputStream = uploaded_file.getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }

                _file_uploaded = true;

                result = true;
            }

        } catch (Exception exc) {           
            throw new AppException(exc.getMessage());
        } finally {

            serviceUtil.close(fileOutputStream);
            serviceUtil.close(inputStream);
        }

        return result;
    }   
    

}
