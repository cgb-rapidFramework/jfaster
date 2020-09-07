package com.abocode.jfaster.core.persistence.hibernate.qbc;

import java.util.ArrayList;

import org.hibernate.criterion.Criterion;

/**
 *
 */
public class CriterionList extends ArrayList<Object> {
    public final Criterion getParas(final int index) {
        return (Criterion) super.get(index);
    }
    public final void addPara(final Criterion p) {
        super.add(p);
    }

}
