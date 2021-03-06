package tp.model.statistics;

import java.util.List;

import javafx.util.Pair;

public class StatisticValues {
	
	private String name;
	private List<Pair<Integer, Integer>> values;
	
	public StatisticValues(String name, List<Pair<Integer, Integer>> values) {
		super();
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pair<Integer, Integer>> getValues() {
		return values;
	}

	public void setValues(List<Pair<Integer, Integer>> values) {
		this.values = values;
	}

}
