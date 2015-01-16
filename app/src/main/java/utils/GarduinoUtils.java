package utils;

import java.io.IOException;

import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Created by Fabian on 16.01.2015.
 */
public class GarduinoUtils {

        private String LOG_TAG = this.getClass().getSimpleName();


        public String objectToJsonString(Object object) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter a = mapper.writerWithType(ArduinoDataBean.class);
            ObjectWriter writerWithDefaultPrettyPrinter = mapper
                    .writerWithDefaultPrettyPrinter();
            String writeValueAsString = null;
            try {
                writeValueAsString = a
                        .writeValueAsString(object);
            } catch (JsonGenerationException e) {
                Log.e(LOG_TAG + " objectToJsonString() - JsonGenerationException", e.getMessage().toString());
                e.printStackTrace();
            } catch (JsonMappingException e) {
                Log.e(LOG_TAG + " objectToJsonString() - JsonMappingException", e.getMessage().toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG + " objectToJsonString() - IOException", e.getMessage().toString());
                e.printStackTrace();
            }
            //writeValueAsString = writeValueAsString.replaceAll("\n", "<br/>");
            return writeValueAsString;
        }

        public ArduinoDataBean StringToBean(String json) {

            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            ArduinoDataBean bean = null;
            //convert json string to object
            try {
                bean = objectMapper.readValue(json, ArduinoDataBean.class);
            } catch (JsonParseException e) {
                Log.e(LOG_TAG + " StringToBean() - JsonParseException", e.getMessage().toString());
                e.printStackTrace();
            } catch (JsonMappingException e) {
                Log.e(LOG_TAG + " StringToBean() - JsonMappingException", e.getMessage().toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG + " StringToBean() - IOException", e.getMessage().toString());
                e.printStackTrace();
            }


            return bean;
        }


}
