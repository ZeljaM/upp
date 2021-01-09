package com.upp.dtos;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlagiatorResponseDTO

{
    private Long id;
	private List<ResultItemDTO> items = new ArrayList<ResultItemDTO>();
	private List<PaperDTO> similarPapers = new ArrayList<PaperDTO>();
	private PaperDTO uploadedPaper;
}
