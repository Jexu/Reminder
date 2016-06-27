package com.tt.sharedutils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhengguo on 5/13/16.
 */
public class FileUtil {

    public static final int BUFFER_SIZE = 2048;

    public static boolean deleteFileOrDirectory( File file ) {
        if ( file == null || file.equals( " " ) ) {
            Log.e( "TAG", "error:file is null" );
            return false;
        }
        if ( file.isDirectory() ) {
            for ( File f : file.listFiles() ) {
                if ( f.isDirectory() ) {
                    deleteFileOrDirectory( f );
                } else if ( f.isFile() ) {
                    f.delete();
                }
            }
            return file.delete();
        } else if ( file.isFile() ) {
            return file.delete();
        }
        return false;
    }

    public static String readFileToString( String filePath ) {
        File file = new File( filePath );
        if ( !file.exists() ) {
            Log.e( "Tag", "error:File does not exist" );
            return null;
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream( file );
            reader = new BufferedReader( new InputStreamReader( fis ) );
            String line = null;
            while ( ( line = reader.readLine() ) != null ) {
                sb.append( line );
            }
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void writeDataToFile( File dir, String fileName, InputStream inputStream ) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if ( dir != null && fileName != null && inputStream != null ) {
                if ( !dir.exists() ) {
                    dir.mkdirs();
                }
                File file = new File( dir, fileName );
                if ( file.exists() ) {
                    deleteFileOrDirectory( file );
                    file.createNewFile();
                }
                bis = new BufferedInputStream( inputStream );
                bos = new BufferedOutputStream( new FileOutputStream( file ) );
                byte buf[] = new byte[BUFFER_SIZE];
                int readLen = 0;
                while ( ( readLen = bis.read( buf ) ) != 0 ) {
                    bos.write( buf, 0, readLen );
                    bos.flush();
                }
            } else {
                Log.e( "Tag", "error:input is null" );
                return;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            try {

                if ( bis != null ) {
                    bis.close();
                }
                if ( bos != null ) {
                    bos.close();
                }
            } catch ( IOException e ) {
                Log.e( "Tag", "error to closing io" );
                e.printStackTrace();
            }
        }
    }

    public static void copyFileToFile( File srcFile, File desFile ) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if ( srcFile != null && desFile != null && srcFile.exists() && srcFile.isFile() ) {
                if ( !desFile.exists() ) {
                    desFile.createNewFile();
                }
                if ( desFile.isFile() ) {
                    bis = new BufferedInputStream( new FileInputStream( srcFile ) );
                    bos = new BufferedOutputStream( new FileOutputStream( desFile ) );
                    byte buf[] = new byte[BUFFER_SIZE];
                    int readLen = 0;
                    while ( ( readLen = bis.read( buf ) ) != 0 ) {
                        bos.write( buf, 0, readLen );
                        bos.flush();
                    }
                } else {
                    Log.e( "TAG", "error:desFile is directory" );
                    return;
                }
            }
        } catch ( IOException e ) {
            Log.e( "TAG", "error:error to createNewFile" );
            e.printStackTrace();
        } finally {
            try {
                if ( bis != null ) {
                    bis.close();
                }
                if ( bos != null ) {
                    bos.close();
                }
            } catch ( IOException e ) {
                Log.e( "TAG", "error:error to closing io" );
                e.printStackTrace();
            }
        }
    }

}