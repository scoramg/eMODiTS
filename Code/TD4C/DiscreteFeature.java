package TD4C;

import java.util.List;

public class DiscreteFeature<S> extends RankedFeature<Integer> {

    private List<S> cutoffs;

    public DiscreteFeature(Object key, Integer defaultValue, DataTable dataTable) {
        super(key, defaultValue, dataTable);
    }

    public List<S> getCutoffs() {
        return cutoffs;
    }

    public void setCutoffs(List<S> cutoffs) {
        this.cutoffs = cutoffs;
    }
}
