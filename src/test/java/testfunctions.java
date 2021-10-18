import it.polimi.tiw.tiw179.HTMLPure.beans.Topic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class testfunctions {

    @Test
    public void testTopicPrint(){
        ArrayList<Topic> topics= new ArrayList<>();
        ArrayList<Topic> topics2= new ArrayList<>();
        ArrayList<Topic> topics3= new ArrayList<>();

        for (int i=1; i<5; i++){
            topics.add(new Topic(i,"A"));
        }
        for (int i=1; i<3; i++){
            topics2.add(new Topic(i*10+i,"B"));
        }
        for (int i=1; i<4; i++){
            topics3.add(new Topic(i*100+i*10+i,"C"));
        }


       /* for(Topic x: topics){
            x.setSubtopics(topics2);
            for(Topic y: x.getSubtopics()){
                y.setSubtopics(topics3);
            }
        }

        for (Topic x: topics){
            Topic.print(x);
        }

        */
    }

    @Test
    public void testDepht(){

    }

}
