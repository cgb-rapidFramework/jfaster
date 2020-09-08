package com.abocode.jfaster.core.persistence.hibernate.qbc;

import org.hibernate.criterion.Criterion;

import java.util.ArrayList;

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
