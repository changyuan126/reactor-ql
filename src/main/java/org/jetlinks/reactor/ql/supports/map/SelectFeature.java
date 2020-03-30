package org.jetlinks.reactor.ql.supports.map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.jetlinks.reactor.ql.DefaultReactorQL;
import org.jetlinks.reactor.ql.ReactorQLMetadata;
import org.jetlinks.reactor.ql.feature.FeatureId;
import org.jetlinks.reactor.ql.feature.ValueMapFeature;
import org.jetlinks.reactor.ql.supports.DefaultReactorQLMetadata;
import org.jetlinks.reactor.ql.supports.ReactorQLContext;
import org.reactivestreams.Publisher;

import java.util.function.Function;


public class SelectFeature implements ValueMapFeature {

    private static String ID = FeatureId.ValueMap.select.getId();

    @Override
    public Function<ReactorQLContext, ? extends Publisher<?>> createMapper(Expression expression, ReactorQLMetadata metadata) {
        SubSelect select = ((SubSelect) expression);

        if (select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            DefaultReactorQLMetadata qlMetadata = new DefaultReactorQLMetadata(plainSelect);
            DefaultReactorQL ql = new DefaultReactorQL(qlMetadata);
            return ctx -> ql.start(ctx::getDataSource);
        }

        throw new UnsupportedOperationException("不支持的嵌套查询:" + expression);
    }

    @Override
    public String getId() {
        return ID;
    }
}