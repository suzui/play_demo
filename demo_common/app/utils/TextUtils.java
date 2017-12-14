package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    public static void main(String[] args) {
        File file = new File("/Users/suzui/Desktop/log.log");
        LineIterator it = null;
        List<String> ids = new ArrayList<>();
        int i=0;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {
                String s = it.next();
                
                if(s.contains("messageId")&&s.contains("考试通知")){
                    if (i<100){
    
                        System.err.println(++i+":"+s);
                    }
                    String messageId=s.substring(s.indexOf("messageId")+11,s.indexOf("messageId")+18);
                    String conversationId=s.substring(s.indexOf("conversationId")+16,s.indexOf("conversationId")+22);
                    String randomId=s.substring(s.indexOf("randomId")+  11,s.indexOf("randomId")+43);
                    String receiverId=s.substring(s.indexOf("receiverJIDs")+15,s.indexOf("result")-3);
                    receiverId=receiverId.replace("worker@wechat.iclass.cn/iclassserver","").replace("@wechat.iclass.cn/iclassserver","").replace(",","");
                    ids.add(messageId+"-"+randomId+"-"+conversationId+"-"+receiverId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.err.println(i);
            System.err.println(ids);
            LineIterator.closeQuietly(it);
        }
        
    }
}

