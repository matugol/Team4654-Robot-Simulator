package org.firstinspires.ftc.robotcore.external;

public interface Telemetry {

	public Telemetry.Item addData(String caption, Object value);
	public Telemetry.Item addData(String caption, String format, Object... args);
	
	public Telemetry.Line addLine();
	public Telemetry.Line addLine(String lineCaption);
	
	public void clear();
	public void clearAll();
	
	public Telemetry.Log log();
	
	public boolean removeItem(Telemetry.Item item);
	public boolean removeLine(Telemetry.Line line);
	
	public void setAutoClear(boolean autoClear);
	
	public boolean update();
	
	public class Item {
		
	}
	
	public class Line {
		
	}
	
	public class Log {
		
	}
	
}
