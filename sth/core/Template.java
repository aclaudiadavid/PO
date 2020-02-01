package sth.core;

import java.util.List;
import java.util.ArrayList;

abstract class Template {
	abstract String header();
	abstract List<String> body();

	public final List<String> getInfo() {
		List<String> l = new ArrayList<>(); 

		l.add(header());
		l.addAll(body());
	
		return l;
	}
}