package com.zfw.core.sys.entity;

/**
 * @Author:zfw
 * @Date:2019/10/11
 * @Content:
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zfw.core.constant.Constant;
import com.zfw.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author:zfw
 * @Date:2019/9/18
 * @Content: excel,zip导入错误信息表，插入这张表中，用于给用户回馈
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="t_import_error_msg")
@Where(clause="del_flag="+ BaseEntity.DEL_FLAG_NORMAL)
public class ImportMsg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type",columnDefinition = "varchar(255) comment '消息类型'")
    private String type;

    @Column(name = "file_name",columnDefinition = "varchar(255) comment '原始导入文件名'")
    private String fileName;

    @Column(name = "error_file_name",columnDefinition = "varchar(255) comment '导入错误的文件名，用于用户下载'")
    private String errorFileName;

    @JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "import_start_time",columnDefinition = "timestamp default now() comment '导入的开始时间'")
    private Date importStartTime;

    @JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
    @Column(name = "import_end_time",columnDefinition = "timestamp default now() comment '导入的结束时间'")
    private Date importEndTime;

    @Column(name = "import_error_msg",columnDefinition = "varchar(255) comment '导入错误的反馈信息'")
    private String importErrorMsg;

    @Column(name="state",columnDefinition = "varchar(8) comment '此信息的处理状态'")
    @Convert(converter = Constant.Convert.class)
    private Constant state= Constant.IMPORT_UNTREATED;

}
