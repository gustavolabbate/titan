package br.com.cpqd.titan.processor;

import java.util.List;

import br.com.cpqd.titan.utils.DBConnection;

/**
 * 
 * @author ggodoy
 *
 */
public class FileTemplate
{
	private List<Fields> headerFields;
	private int headerLines;
	private Boolean headerShuffle;
        
	private List<Fields> trailFields;
	private int trailLines;
	private Boolean trailShuffle;
	
        private List<Fields> footerFields;
	private Boolean footerShuffle;
	private int footerLines;
	
        private String fileName;
	private String fileAppend;
	private String betweenLine;
	private String delimiter;
	private int filesQtd;
	private DBConnection dbConn;
	private String whereMySeed;
	
	public final List<Fields> getHeaderFields() {
		return headerFields;
	}

	public final void setHeaderFields(List<Fields> headerFields) {
		this.headerFields = headerFields;
	}

	public final List<Fields> getTrailFields() {
		return trailFields;
	}

	public final void setTrailFields(List<Fields> trailFields) {
		this.trailFields = trailFields;
	}

	public final List<Fields> getFooterFields() {
		return footerFields;
	}

	public final void setFooterFields(List<Fields> footerFields) {
		this.footerFields = footerFields;
	}

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public final int getHeaderLines() {
		return headerLines;
	}

	public final void setHeaderLines(int headerLines) {
		this.headerLines = headerLines;
	}

	public final int getTrailLines() {
		return trailLines;
	}

	public final void setTrailLines(int trailLines) {
		this.trailLines = trailLines;
	}

	public final int getFooterLines() {
		return footerLines;
	}

	public final void setFooterLines(int footerLines) {
		this.footerLines = footerLines;
	}

	public final int getFilesQtd() {
		return filesQtd;
	}

	public final void setFilesQtd(int filesQtd) {
		this.filesQtd = filesQtd;
	}

	public final Boolean getHeaderShuffle() {
		return headerShuffle;
	}

	public final void setHeaderShuffle(Boolean headerShuffle) {
		this.headerShuffle = headerShuffle; 
	}

	public final Boolean getTrailShuffle() {
		return trailShuffle;
	}

	public final void setTrailShuffle(Boolean trailShuffle) {
		this.trailShuffle = trailShuffle;
	}

	public final Boolean getFooterShuffle() {
		return footerShuffle;
	}

	public final void setFooterShuffle(Boolean footerShuffle) {
		this.footerShuffle = footerShuffle;
	}

	public final String getFileAppend() {
		return fileAppend;
	}

	public final void setFileAppend(String fileAppend) {
		this.fileAppend = fileAppend;
	}

	public final String getBetweenLine() {
		return betweenLine;
	}

	public final void setBetweenLine(String betweenLine) {
		this.betweenLine = betweenLine;
	}

	public final String getDelimiter() {
		return delimiter;
	}

	public final void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public final String getWhereMySeed() {
	    return whereMySeed;
	}

	public final void setWhereMySeed(String whereMySeed) {
	    this.whereMySeed = whereMySeed;
	}

	public DBConnection getDbConn() {
		return dbConn;
	}

	public void setDbConn(DBConnection dbConn) {
		this.dbConn = dbConn;
	}


}
