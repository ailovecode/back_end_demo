package edu.zut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zut.entity.OperationLog;
import edu.zut.service.OperationLogService;
import edu.zut.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【operation_logs】的数据库操作Service实现
* @createDate 2025-06-21 21:34:34
*/
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService {

}




