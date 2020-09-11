package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.dto.HighChartDto;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.ChartService;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
@Service
public class ChartServiceImpl implements ChartService {
    @Resource
    private SystemRepository systemService;
    //用户浏览器统计分析的国际化KEY
    private static final String USER_BROWSER_ANALYSIS = "user.browser.analysis";
    @Override
    public List<HighChartDto> buildChart(String reportType) {
        List<HighChartDto> list = new ArrayList<HighChartDto>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT broswer ,count(broswer) FROM Log group by broswer");
        List userBroswerList = systemService.findByHql(sb.toString());
        Long count = systemService.queryForCount("SELECT COUNT(1) FROM T_S_Log WHERE 1=1");
        List lt = new ArrayList();
        HighChartDto hc = new HighChartDto();
        hc.setName(LanguageUtils.getLang(USER_BROWSER_ANALYSIS));
        hc.setType(reportType);
        Map<String, Object> map;
        if (userBroswerList.size() > 0) {
            for (Object object : userBroswerList) {
                map = new HashMap();
                Object[] obj = (Object[]) object;
                map.put("name", obj[0]);
                map.put("y", obj[1]);
                Long groupCount = (Long) obj[1];
                long percentage = 0;
                if (count != null && count.intValue() != 0) {
                    percentage = groupCount/count;
                }
                map.put("percentage", percentage*100);
                lt.add(map);
            }
        }
        hc.setData(lt);
        list.add(hc);
        return list;
    }
}
