package com.personal.springboot.gataway.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenApiHandlerExecuteTemplate implements Chain {
	private static final Logger logger = LoggerFactory
			.getLogger(OpenApiHandlerExecuteTemplate.class);
	
	private List<Command> commands = new ArrayList<Command>();
	
	@Override
	public void addCommand(Command command) {
		commands.add(command);
	}
	
	public OpenApiHandlerExecuteTemplate(List<Command> commands) {
		if (commands == null) {
			throw new IllegalArgumentException();
		}
		Iterator<Command> elements = commands.iterator();
		while (elements.hasNext()) {
			addCommand((Command) elements.next());
		}
	}
	
	@Override
	public boolean execute(Context context) throws Exception {
//		logger.info("executing all handlers,have a good journey!");
		if (context == null || null == this.commands) {
			throw new IllegalArgumentException();
		}
		Iterator<Command> cmdIterator = commands.iterator();
		Command cmd = null;
		while(cmdIterator.hasNext()){
			cmd = (Command) cmdIterator.next();
			cmd.execute(context);
		}
		return false;
	}
}
