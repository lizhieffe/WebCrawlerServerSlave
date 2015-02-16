package com.zl.tasks;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Class with static methods that can save URLs and extract links
 * Works also as a standalone program, usage is: java SaveURL <url> [<file>]
 * If file is not specified, all links on the specified url are printed to the
 * standard console.
 *
 * This code is in the public domain.
 *
 * @author Andreas Hess <andreas.hess@ucd.ie>, 01/02/2003
 */

public class CrawlWebContentTaskHelper
{

	/**
	 * Opens a buffered stream on the url and copies the contents to writer
	 */
	public static void saveURL(URL url, Writer writer)
		throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		for (int c = in.read(); c != -1; c = in.read()) {
			writer.write(c);
		}
	}

	/**
	 * Opens a buffered stream on the url and copies the contents to OutputStream
	 */
	public static void saveURL(URL url, OutputStream os)
		throws IOException {
		InputStream is = url.openStream();
		byte[] buf = new byte[1048576];
		int n = is.read(buf);
		while (n != -1) {
			os.write(buf, 0, n);
			n = is.read(buf);
		}
	}

	/**
	 * Writes the contents of the url to a string by calling saveURL with a
	 * string writer as argument
	 */
	public static String getURL(URL url)
		throws IOException {
		StringWriter sw = new StringWriter();
		saveURL(url, sw);
		return sw.toString();
	}

	/**
	 * Writes the contents of the url to a new file by calling saveURL with
	 * a file writer as argument
	 */
	public static void writeURLtoFile(URL url, String filename)
		throws IOException {
		// FileWriter writer = new FileWriter(filename);
		// saveURL(url, writer);
		// writer.close();
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}

	/**
	 * Extract links directly from a URL by calling extractLinks(getURL())
	 */
	public static List<String> extractLinks(URL url)
		throws IOException {
		return extractLinks(getURL(url));
	}

	public static Map extractLinksWithText(URL url)
		throws IOException {
		return extractLinksWithText(getURL(url));
	}

	/**
     * Extract links from a html page given as a raw and a lower case string
     * In order to avoid the possible double conversion from mixed to lower case
     * a second method is provided, where the conversion is done externally.
     */
    public static List<String> extractLinks(String rawPage, String page) {
		int index = 0;
		List<String> links = new ArrayList<String>();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
		    if ((index = page.indexOf("href", index)) == -1)
		    	break;
		    if ((index = page.indexOf("=", index)) == -1) 
		    	break;
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			if (! links.contains(strLink)) 
				links.add(strLink);
		}
		return links;
    }

	/**
	 * Extract links (key) with link text (value)
	 * Note that due to the nature of a Map only one link text is returned per
	 * URL, even if a link occurs multiple times with different texts.
	 */
	public static Map extractLinksWithText(String rawPage, String page) {
		int index = 0;
		Map links = new HashMap();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
			int tagEnd = page.indexOf(">", index);
		    if ((index = page.indexOf("href", index)) == -1) break;
		    if ((index = page.indexOf("=", index)) == -1) break;
			int endTag = page.indexOf("</a", index);
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st 
				= new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			String strText = "";
			if (tagEnd != -1 && tagEnd + 1 <= endTag) {
				strText = rawPage.substring(tagEnd + 1, endTag);
			}
			strText = strText.replaceAll("\\s+", " ");
			links.put(strLink, strText);
		}
		return links;
		
	}
    
    /**
	 * Extract links from a html page given as a String
	 * The return value is a vector of strings. This method does neither check
	 * the validity of its results nor does it care about html comments, so
	 * links that are commented out are also retrieved.
	 */
	public static List<String> extractLinks(String rawPage) {
        return extractLinks(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	public static Map extractLinksWithText(String rawPage) {
        return extractLinksWithText(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	public static List<URL> getContainedURL(String rawPage) {
		List<URL> result = new ArrayList<URL>();

        // I don't know if it is legal, but we also want to identify the
        // file as WSDL if the definitions-tag is not spelled in small
        // letters.
        String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");
        
		if (smallPage.indexOf("<definitions") != -1) {
//			// assume that it's a wsdl
//			String filename = pageURL.getPath();
//            // add a unique number as filename prefix
//            int serviceId = tc.getUniqueNumber();
//            filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
//                "-" + filename.substring(filename.lastIndexOf('/') + 1);
//            // add suffix if not already there
//            if (!filename.toLowerCase().endsWith(".wsdl"))
//                filename += ".wsdl";
//			FileWriter fw = new FileWriter(filename);
//			fw.write(rawPage);
//			fw.close();
//            // now try to retrieve the home page for the wsdl we have just
//            // downloaded and store it as well
//            URL homepage = new URL(pageURL.getProtocol(),
//                pageURL.getHost(),
//                "");    // leave filename blank
//            filename = ((URLQueue) queue).getFilenamePrefix() + serviceId +
//                "-" + homepage.getHost() + ".html";
//            SaveURL.writeURLtoFile(homepage, filename);
		} else {
			// treat the url a a html file and try to extract links
			List<String> links = extractLinks(rawPage, smallPage);
			// Convert each link text to a url and enque
			for (int n = 0; n < links.size(); n++) {
				try {
					// urls might be relative to current page
					URL link = new URL(links.get(n));
					result.add(link);
				} catch (MalformedURLException e) {
					// Ignore malformed URLs, the link extractor might
					// have failed.
					
//					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
