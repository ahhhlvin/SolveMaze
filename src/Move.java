import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvin2 on 4/9/16.
 * Alvin Kuang
 * C4Q Access Code 2.1
 */
public class Move
{
    String letterStep;
    List<Step> adjacentSteps = new ArrayList<Step>();
    boolean end;
    boolean visited = false;


    public Move () {
    }

    public Move (String letterStep, List<Step> adjacentSteps, boolean end) {
        this.letterStep = letterStep;
        this.adjacentSteps = adjacentSteps;
        this.end = end;
    }

    public String getLetterStep()
    {
        return letterStep;
    }

    public void setLetterStep(String letterStep)
    {
        this.letterStep = letterStep;
    }

    public List<Step> getAdjacentSteps()
    {
        return adjacentSteps;
    }

    public void setAdjacentSteps(List<Step> adjacentSteps)
    {
        this.adjacentSteps = adjacentSteps;
    }

    public boolean isEnd()
    {
        return end;
    }

    public void setEnd(boolean end)
    {
        this.end = end;
    }
}
