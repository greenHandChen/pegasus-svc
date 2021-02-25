package com.pegasus.file.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by enHui.Chen on 2021/2/19.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pe_file")
public class PeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    @Column
    private String fileName;
    @Column
    private String filePath;
    @Column
    private String fileType;
    @Column
    private String fileLength;
    @Column
    private String currentLength;
    @Column
    private String fileMd5;
}
