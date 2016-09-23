package br.com.cpqd.titan.main;

import java.util.List;

import br.com.cpqd.titan.processor.FileTemplate;
import br.com.cpqd.titan.processor.GenerateDataThreads;
import br.com.cpqd.titan.processor.TemplateProcessor;

/**
 * 
 * ThreadsGenerate Class (test)
 *
 */

final class ThreadsGenerate {

    	private ThreadsGenerate(){};
	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) 
	{
		TemplateProcessor template;
		List<FileTemplate> fileRead;

		try {
		    template = new TemplateProcessor(args[0]);
                    fileRead = template.readTemplate(false);
		

		//gerar 4 threads para cada arquivo a ser criado


		for(int y=0; y<fileRead.size(); y++)
		{
			Thread thread = new Thread(new GenerateDataThreads(fileRead.get(y)), "thread1 do arquivo "+y);
			Thread thread1 = new Thread(new GenerateDataThreads(fileRead.get(y)), "thread2 do arquivo "+y);
			Thread thread2 = new Thread(new GenerateDataThreads(fileRead.get(y)), "thread3 do arquivo "+y);
			Thread thread3 = new Thread(new GenerateDataThreads(fileRead.get(y)), "thread4 do arquivo "+y);
			thread.start();
			thread1.start();
			thread2.start();
			thread3.start();
		}

		} catch (Exception e) {
		    e.getMessage();
		 
		}




	}

}
