package br.com.cpqd.titan.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.MaskFormatter;

/**
 * 
 * @author ggodoy
 *
 */
public final class Utils
{
    
    private Utils(){};
	/**
	 * 
	 * @return
	 */
    	public static String getDateTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("[dd-MM-yyyy HHmmss]");
		Date date = new Date();
		return dateFormat.format(date);
	}
    	
    	/**
    	 * 
    	 * @return
    	 */
	public static String getDateTimeH()
	{
		DateFormat dateFormat = new SimpleDateFormat("[dd\\MM\\yyyy HH:mm:ss]");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTime(Date date)
	{
		DateFormat dateFormat = new SimpleDateFormat("[dd-MM-yyyy HHmmss]");
		return dateFormat.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeH(Date date)
	{
		DateFormat dateFormat = new SimpleDateFormat("[dd\\MM\\yyyy HH:mm:ss]");
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateTime(String format)
	{
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @return
	 */
	public static String getDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @return
	 */
	public static String getTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Converte uma string no formato de outra string.
	 *
	 * @param  pattern : string que cont�m o formato desejado
	 * @param  toFormat : string que ser� formatada pelo pattern
	 * @return      a String formatada pelo formato da String pattern
	 *
	 */
	public static String convertNumber(String  pattern, String toFormat)
	{
		if(!(toFormat.trim().equals("null")))
		{
		DecimalFormatSymbols smb = new DecimalFormatSymbols();
		smb.setDecimalSeparator('.');
		double data;
		String separator = null;
		StringBuffer format = new StringBuffer();

		if(pattern.contains(","))
		{
			separator=",";
			smb.setDecimalSeparator(',');
		}

		if(pattern.contains("."))
		{
			separator="\\.";
		}

		if(separator==null)
		{
			for(int i=0;i<pattern.trim().length();i++)
			{
				format.append("0");
			}

			DecimalFormat df1 = new DecimalFormat(format.toString(), smb);
			data = new Double(toFormat);
			toFormat = df1.format(data);
			return toFormat;
		}
		else
		{
			String[] formatTemplate  = pattern.split(separator);
			for(int x=0 ; x< formatTemplate[0].length() ; x++ )
			{
				format.append("0");
			}


			format.append(".");

			for(int x=0 ; x< formatTemplate[1].length() ; x++ )
			{
				format.append("0");
			}

			DecimalFormat df1 = new DecimalFormat(format.toString(), smb);
			data = new Double(toFormat);
			toFormat = df1.format(data);
			return toFormat;
		}
		}
		return "null";
	}


	/**
	 * Converte uma string de data no formato desejado.
	 *
	 * @param  format : formato desejado
	 * 					(ano: yyyy
	 * 					mes: MM
	 * 					dia: dd
	 * 					hora: HH
	 * 					minuto: mm
	 * 					segundos: ss)
	 * @param  toFormat : string de data que ser� formatada pelo format
	 * @return      a string de data formatada pelo formato informado
	 * @throws ParseException 
	 *
	 */
	public static String convertData(String format, String toFormat) throws ParseException 
	{
		SimpleDateFormat dataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatDate = new SimpleDateFormat(format);

		toFormat = formatDate.format(dataBaseFormat.parse(toFormat));
		return toFormat;
	}

	/**
	 * 
	 * @return
	 */
	public static String generateCnpj()
	{

		int[] n = new int[12];

		for(int x=0; x<n.length-4; x++)
		{
			n[x] = (int)(Math.random() * 9);
		}
		n[8] = 0;
		n[9] = 0;
		n[10] = 0;
		n[11] = 1;

	    int d1 = n[11] * 2 + n[10] * 3 + n[9] * 4 + n[8] * 5 + n[7] * 6 + n[6] * 7 + n[5] * 8 + n[4] * 9 + n[3] * 2 + n[2] * 3 + n[1] * 4 + n[0] * 5;
	    d1 = 11 - d1 % 11;
	    d1 = d1 >= 10 ? 0 : d1;

	    int d2 = d1 * 2 + n[11] * 3 + n[10] * 4 + n[9] * 5 + n[8] * 6 + n[7] * 7 + n[6] * 8 + n[5] * 9 + n[4] * 2 + n[3] * 3 + n[2] * 4 + n[1] * 5 + n[0] * 6;
	    d2 = 11 - d2 % 11;
	    d2 = d2 >= 10 ? 0 : d2;

	    StringBuffer cnpj = new StringBuffer();

	    for(int x=0; x<n.length; x++)
	    {
	    	cnpj.append(Integer.toString(n[x]));
	    }
	    cnpj.append(Integer.toString(d1));
	    cnpj.append(Integer.toString(d2));

	    return cnpj.toString().trim();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String maskCnpj() throws Exception
	{
		String cnpj = generateCnpj();

		MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
		mf.setValueContainsLiteralCharacters(false);
		cnpj = mf.valueToString(cnpj);

		return cnpj.trim();
	}

	/**
	 * 
	 * @return
	 */
	public static String generateCpf()
	{
		int[] n = new int[9];

		for(int x=0; x<n.length; x++)
		{
			n[x] = (int)(Math.random() * 9);
		}

		int d1 = n[8] * 2 + n[7] * 3 + n[6] * 4 + n[5] * 5 + n[4] * 6 + n[3] * 7 + n[2] * 8 + n[1] * 9 + n[0] * 10;
	    d1 = 11 - d1 % 11;
	    d1 = d1 >= 10 ? 0 : d1;

		int d2 = d1 * 2 + n[8] * 3 + n[7] * 4 + n[6] * 5 + n[5] * 6 + n[4] * 7 + n[3] * 8 + n[2] * 9 + n[1] * 10 + n[0] * 11;
		d2 = 11 - d2 % 11;
		d2 = d2 >= 10 ? 0 : d2;

		StringBuffer cpf = new StringBuffer();
		for(int x=0; x<n.length; x++)
		{
			cpf.append(Integer.toString(n[x]));
		}
		cpf.append(Integer.toString(d1));
		cpf.append(Integer.toString(d2));

		return cpf.toString().trim();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String maskCpf() throws Exception
	{
		String cpf = generateCpf();

		MaskFormatter mf = new MaskFormatter("###.###.###-##");
		mf.setValueContainsLiteralCharacters(false);
		cpf = mf.valueToString(cpf);

		return cpf.trim();
	}
        
        public static Calendar setCalendarAsFirstHour()
        {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
        }
        
        public static Calendar setCalendarAsLastHour()
        {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar;
        }

}
