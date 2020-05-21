package com.abocode.jfaster.admin.system.repository;
import com.abocode.jfaster.core.repository.CommonRepository;
public interface RepairRepository extends CommonRepository {
	void deleteAndRepair();
}
