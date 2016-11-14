package com.example.kouding.class_practiceone_log;

/**
 * Created by kouding on 2016/11/3.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class Utils {

    private static final String FILENAME = "userInfo.xml"; // 用户保存文件名
    private static final String TAG = "Utils";

    /* 保存用户登录信息列表 */
    public static void saveUserList(Context context, ArrayList<User> users)
            throws Exception {
        /* 保存 */
        Log.i(TAG, "正在保存");
        OutputStream out = null;
        XmlSerializer serializer = Xml.newSerializer();
        try {
        out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE); // 覆盖
        serializer.setOutput(out, "UTF-8");

        //缩进
        serializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output",
                true);
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "Users");
        for (User user : users) {
            serializer.startTag(null, "user");//开始节点

            //一个新的节点
            serializer.startTag(null, "name");
            serializer.text(user.getName());
            serializer.endTag(null, "name");

            //一个新的节点
            serializer.startTag(null, "password");
            serializer.text(user.getPwd() + "");
            serializer.endTag(null, "password");

            serializer.endTag(null, "user");//结束节点
        }
        }
        catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

        serializer.endTag(null, "Users");//结束节点
            serializer.endDocument();//对应startDocument
            out.flush();//刷新流
            out.close();//关闭流

    }

    /* 获取用户登录信息列表 */
    public static ArrayList<User> getUserList(Context context) {

          //  OutputStream out=null;
           // try {
             //   out = context.openFileOutput(FILENAME,context.MODE_WORLD_READABLE);
              //  out.flush();//刷新流
               // out.close();//关闭流
            //} catch (FileNotFoundException e) {
             //   e.printStackTrace();
            //} catch (IOException e) {
              //  e.printStackTrace();
            //}

        String PACKAGE= context.getPackageName();
        FileInputStream inStream = null;
        XmlPullParser parser = Xml.newPullParser();
        ArrayList<User> users = null;
        File file = new File(context.getFilesDir(),FILENAME);
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            inStream = context.openFileInput(FILENAME);
            parser.setInput(inStream, "UTF-8");// 设置数据源编码
            int eventType = parser.getEventType();// 获取事件类型
            User currentUser = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        users = new ArrayList<User>();// 实例化集合类
                        break;
                    case XmlPullParser.START_TAG://开始读取某个标签
                        //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("user"))
                            currentUser = new User();

                        else if (name.equalsIgnoreCase("name")) {
                                currentUser.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
                            }
                        else if (name.equalsIgnoreCase("password")) {
                                currentUser.setPwd(parser.nextText());
                            }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个user，可以将其添加到集合类中
                        if (parser.getName().equalsIgnoreCase("user") && currentUser != null) {
                            users.add(currentUser);
                            currentUser = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inStream.close();
        }catch (FileNotFoundException e) {
            Log.d(TAG, "file not found exception!");
             e.printStackTrace();
        }
        catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException!");
            e.printStackTrace();
        }
        return users;
    }

}