package org.titan.processor;

import org.apache.log4j.Logger;

/**
 * 
 * @author glabbate
 *
 */
public class GenerateDataThreads implements Runnable
{
    	private static final Logger log = Logger.getLogger(GenerateDataThreads.class.getName());	
	private FileTemplate file;
	
	/**
	 * 
	 * @param file
	 */
	public GenerateDataThreads(FileTemplate file) {
	
		this.file = file;
	}

	/**
	 * 
	 */
	public final void run()
	{
		try {
			log.info(" Gerando arquivo: "+ file.getFileName());
			TemplateProcessor template;
                        template = new TemplateProcessor(file.getFileName());
//			template.generateData(file, false, 0);
			log.info( " Arquivo Gerado: "+ file.getFileName());
		} catch (Exception e) {
			log.error(" Erro ao gravar o arquivo.");
			log.error(e.getMessage(), e);
		}
	}


	/**
	 * 
	 * @return
	 */
	public final FileTemplate getFile() {
		return file;
	}


	/**
	 * 
	 * @param file
	 */
	public final void setFile(FileTemplate file) {
		this.file = file;
	}


}
