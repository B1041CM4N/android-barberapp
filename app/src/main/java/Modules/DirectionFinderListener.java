package Modules;

import java.util.List;

/**
 * Created by Christian on 31-10-2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
