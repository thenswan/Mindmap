package mindmap.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import mindmap.model.BubbleAreaBean;
import mindmap.model.BubbleBean;
import mindmap.model.ConnectorBean;

import java.io.IOException;
import java.util.ArrayList;

public class BubbleAreaBeanDeserializer extends JsonDeserializer<BubbleAreaBean>{

    @Override
    public BubbleAreaBean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        BubbleAreaBean bubbleAreaBean = new BubbleAreaBean();
        ArrayList<BubbleBean> bubbleArrayList = new ArrayList<>();
        ArrayList<ConnectorBean> connectorBeanArrayList = new ArrayList<>();
        bubbleAreaBean.setBubbleArrayList(bubbleArrayList);
        bubbleAreaBean.setConnectorBeanArrayList(connectorBeanArrayList);

        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode bubbleArrayListNode =  rootNode.get("bubbleArrayList");

        for (JsonNode bubbleBeanNode : bubbleArrayListNode) {
            BubbleBean bubbleBean = new BubbleBean();

            double bubbleX = bubbleBeanNode.get("bubbleX").asDouble();
            double bubbleY = bubbleBeanNode.get("bubbleY").asDouble();

            String bubbleText = bubbleBeanNode.get("bubbleText").asText();

            JsonNode bubbleColorNode = bubbleBeanNode.get("bubbleColor");
            double red = bubbleColorNode.get("red").asDouble();
            double green = bubbleColorNode.get("green").asDouble();
            double blue = bubbleColorNode.get("blue").asDouble();
            double opacity = bubbleColorNode.get("opacity").asDouble();

            JsonNode bubbleFontNode = bubbleBeanNode.get("bubbleFont");
            String family = bubbleFontNode.get("family").asText();
            String size = bubbleFontNode.get("size").asText();

            bubbleBean.setBubbleX(bubbleX);
            bubbleBean.setBubbleY(bubbleY);
            bubbleBean.setBubbleText(bubbleText);
            bubbleBean.setBubbleColor(Paint.valueOf(String.valueOf(Color.color(red, green, blue, opacity))));
            bubbleBean.setBubbleFont(Font.font(family, Double.parseDouble(size)));

//            System.out.println(bubbleBean.toString());

            bubbleArrayList.add(bubbleBean);
        }

        JsonNode connectorBeanArrayListNode =  rootNode.get("connectorBeanArrayList");

        for (JsonNode connectorBeanNode : connectorBeanArrayListNode) {
            ConnectorBean connectorBean = new ConnectorBean();

            double startX = connectorBeanNode.get("startX").asDouble();
            double startY = connectorBeanNode.get("startY").asDouble();
            double controlX1 = connectorBeanNode.get("controlX1").asDouble();
            double controlY1 = connectorBeanNode.get("controlY1").asDouble();
            double controlX2 = connectorBeanNode.get("controlX2").asDouble();
            double controlY2 = connectorBeanNode.get("controlY2").asDouble();
            double endX = connectorBeanNode.get("endX").asDouble();
            double endY = connectorBeanNode.get("endY").asDouble();

            connectorBean.setStartX(startX);
            connectorBean.setStartY(startY);
            connectorBean.setControlX1(controlX1);
            connectorBean.setControlY1(controlY1);
            connectorBean.setControlX2(controlX2);
            connectorBean.setControlY2(controlY2);
            connectorBean.setEndX(endX);
            connectorBean.setEndY(endY);

//            System.out.println(connectorBean.toString());

            connectorBeanArrayList.add(connectorBean);
        }

        return bubbleAreaBean;
    }
}