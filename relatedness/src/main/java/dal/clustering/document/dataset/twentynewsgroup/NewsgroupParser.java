package dal.clustering.document.dataset.twentynewsgroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NewsgroupsArticle implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat[] dateFormats = new SimpleDateFormat[]{
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss"),
            new SimpleDateFormat("dd MMM yyyy HH:mm z"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm z"),
            new SimpleDateFormat("E, dd MMM yy HH:mm:ss z")
    };
    private Map<String, String> headers = new HashMap<String, String>();
    private String rawText;
    private String label;
    private Date date;

    public NewsgroupsArticle() {
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate() {
        if (date == null) {
            String dateString = headers.get("Date");
            if (dateString != null) {
                dateString = dateString.trim();

                /*
                    some articles have the UT timezone format, which is not parsed by
                    SimpleDateFormat, hence replacing it with UTC
                 */
                dateString = dateString.replaceFirst("UT$", "UTC");
                date = tryToParseDate(dateString);
            }
        }
        return date;
    }

    private Date tryToParseDate(String dateString) {
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                Date parsedDate = dateFormat.parse(dateString);
                return parsedDate;
            }
            catch (ParseException e) {
            }
        }
        throw new RuntimeException("Date format of " + dateString + " unknown!");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}

public class NewsgroupParser {

	public static void main(String[] args) throws IOException {
		NewsgroupParser parser = new NewsgroupParser("C:\\Users\\mona\\Desktop\\mini_newsgroups\\mini_newsgroups\\");
        parser.parse();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\mona\\Desktop\\mini_newsgroups\\all-data"));

        parser.getArticles().forEach((key, articles) -> {
            System.out.println(key+": "+articles.size());
            articles.forEach(a -> {
//                Date d = a.getDate();
//                if (d == null) {
//                    System.out.println("\t"+a.getHeader("Subject"));
//                    System.out.println("\t"+a.getHeader("Message-ID"));
//                    System.out.println("\tNo date present.");
//                }
//                else
                {
                	System.out.println(a.getRawText());
                	try {
						bw.write(a.getRawText()+"\n");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
        });
        
        bw.close();
    }

    private Path folder;
    private Map<String, List<NewsgroupsArticle>> articles = new HashMap<String, List<NewsgroupsArticle>>();

    public NewsgroupParser(Path folder) {
        this.folder = folder.toAbsolutePath();
    }

    public NewsgroupParser(String folder) {
        this.folder = Paths.get(folder).toAbsolutePath();
    }

    public Map<String, List<NewsgroupsArticle>> getArticles() {
        return articles;
    }

    public void parse() throws IOException {
        try (DirectoryStream<Path> categoriesDirs = Files.newDirectoryStream(folder)) {
            for (Path category : categoriesDirs) {
                String newsgroups = category.getFileName().toString();
                articles.put(newsgroups, new ArrayList<NewsgroupsArticle>());

                try (DirectoryStream<Path> articleFiles = Files.newDirectoryStream(category)) {
                    for (Path articleFile : articleFiles) {
                        try (BufferedReader br = new BufferedReader(new FileReader(articleFile.toString()))){
                            NewsgroupsArticle article = new NewsgroupsArticle();
                            article.setLabel(newsgroups);
                            StringBuffer content = new StringBuffer();
                            String line;
                            boolean inContent = false;
                            while ((line = br.readLine()) != null) {
                                if (!inContent) {
                                    if (line.trim().length() == 0) {
                                        inContent = true;
                                        continue;
                                    }

                                    String[] split = line.split(": ");
                                    if (split.length == 2) {
                                        article.addHeader(split[0], split[1]);
                                    }

                                }
                                else {
                                   
                                	   if(line.contains("In article <") || line.contains("wrote:") || line.contains("writes:")
                                			   || line.contains("From article")|| line.contains("<<****Strong opinions start here...****>>")){
                                       	continue;
                                       }
                                	
                                	content.append(line);
                                    content.append(" ");
                                }
                            }
                            article.setRawText(content.toString().trim());
                            articles.get(newsgroups).add(article);
                        }
                    }
                }
            }
        }
}
	
}
