package qdtContactManagement;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.stream.Collectors;
// --- <<IS-END-IMPORTS>> ---

public final class services

{
	// ---( internal utility methods )---

	final static services _instance = new services();

	static services _newInstance() { return new services(); }

	static services _cast(Object o) { return (services)o; }

	// ---( server methods )---




	public static final void jsonToJavaDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(jsonToJavaDate)>> ---
		// @sigtype java 3.5
		// [i] field:0:required jsonDate
		// [o] object:0:required javaDate
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	jsonDate = IDataUtil.getString( pipelineCursor, "jsonDate" );
		pipelineCursor.destroy();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		Date date = null;
		try {
		date = formatter.parse(jsonDate); 
		} catch (ParseException e) {
		throw new ServiceException(e.getMessage());
		}
		
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		//Object javaDate = new Object();
		IDataUtil.put( pipelineCursor_1, "javaDate", date );
		pipelineCursor_1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void stringListToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stringListToString)>> ---
		// @sigtype java 3.5
		// [i] field:1:required list
		// [o] field:0:required string
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String[]	list = IDataUtil.getStringArray( pipelineCursor, "list" ); 
		pipelineCursor.destroy();
		
		StringJoiner joiner = new StringJoiner(", ");
		for (String element : list) {
		    joiner.add(element);
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "string", joiner.toString() );
		pipelineCursor_1.destroy();
			
		// --- <<IS-END>> ---

                
	}
}

