package com.abocode.jfaster.core.persistence.hibernate.hqlsearch;

import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlParseEnum;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlRuleEnum;

/**
 * 判断类型,拼接字符串
 * 
 * @author JueYue
 * @date 2014年8月23日 下午4:07:34
 */
public class ObjectParseUtil {

	public static void addCriteria(CriteriaQuery cq, String name,
                                   HqlRuleEnum rule, Object value) {
		if (value == null || rule == null) {
			return;
		}
		switch (rule) {
		case GT:
			cq.gt(name, value);
			break;
		case GE:
			cq.ge(name, value);
			break;
		case LT:
			cq.lt(name, value);
			break;
		case LE:
			cq.le(name, value);
			break;
		case EQ:
			cq.eq(name, value);
			break;
		case NE:
			cq.notEq(name, value);
			break;
		case IN:
			cq.in(name, (Object[]) value);
			break;
		case LIKE:
			cq.like(name, HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue() + value
					+ HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue());
			break;
		case LEFT_LIKE:
			cq.like(name, HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue() + value);
			break;
		case RIGHT_LIKE:
			cq.like(name, value + HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue());
			break;
		default:
			break;
		}
	}

}