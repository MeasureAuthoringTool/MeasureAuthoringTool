package cqltoelm.parsers;

import org.antlr.v4.runtime.ParserRuleContext;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlParser.ExpressionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.FunctionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ParameterDefinitionContext;
import org.hl7.elm.r1.VersionedIdentifier;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class TrackbackListener extends cqlBaseListener {

    private VersionedIdentifier identifier;
    private Map<String, TrackBack> trackbackMap = new HashMap<>();

    public TrackbackListener(VersionedIdentifier identifier) {
        super();
        this.identifier = identifier;
    }

    @Override
    public void enterParameterDefinition(@NotNull ParameterDefinitionContext ctx) {
        this.trackbackMap.put(parseString(ctx.identifier().getText()), getTrackBack(ctx));
    }

    @Override
    public void enterExpressionDefinition(@NotNull ExpressionDefinitionContext ctx) {
        this.trackbackMap.put(parseString(ctx.identifier().getText()), getTrackBack(ctx));
    }

    @Override
    public void enterFunctionDefinition(@NotNull FunctionDefinitionContext ctx) {
        this.trackbackMap.put(parseString(ctx.identifierOrFunctionIdentifier().getText()), getTrackBack(ctx));
    }

    private TrackBack getTrackBack(ParserRuleContext ctx) {
        TrackBack tb = new TrackBack(
                identifier,
                ctx.getStart().getLine(),
                ctx.getStart().getCharPositionInLine() + 1, // 1-based instead of 0-based
                ctx.getStop().getLine(),
                ctx.getStop().getCharPositionInLine() + ctx.getStop().getText().length() // 1-based instead of 0-based
        );
        return tb;
    }

    public String parseString(String identifier) {
        return identifier.replaceAll("\"", "");
    }

    public Map<String, TrackBack> getTrackbackMap() {
        return trackbackMap;
    }
}
