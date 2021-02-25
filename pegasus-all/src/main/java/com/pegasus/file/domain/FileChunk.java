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
@Table(name = "pe_file_chunk")
public class FileChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileChunkId;
    @Column
    private Long fileId;
    @Column
    private Long chunkIndex;
    @Column
    private Long chunkLength;
    @Column
    private String chunkStatus;
    @Column
    private String fileChunkMd5;
}
