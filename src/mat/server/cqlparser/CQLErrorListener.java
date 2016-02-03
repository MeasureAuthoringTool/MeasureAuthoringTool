package mat.server.cqlparser;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import mat.shared.CQLErrors;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class CQLErrorListener implements ANTLRErrorListener {

	private List<CQLErrors> errors = new ArrayList<CQLErrors>();

	public CQLErrorListener() {
		errors = new ArrayList<CQLErrors>();
	}

	@Override
	public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3,
			boolean arg4, BitSet arg5, ATNConfigSet arg6) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2,
			int arg3, BitSet arg4, ATNConfigSet arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2,
			int arg3, int arg4, ATNConfigSet arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2,
			int arg3, String arg4, RecognitionException arg5) {
		CQLErrors error = new CQLErrors();
		error.setErrorInLine(arg2);
		error.setErrorAtOffeset(arg3);
		error.setErrorMessage(arg4);
		errors.add(error);
	}

	public List<CQLErrors> getErrors() {
		return errors;
	}

}
