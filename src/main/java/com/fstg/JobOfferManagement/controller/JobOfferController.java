package com.fstg.JobOfferManagement.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;



import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fstg.JobOfferManagement.dto.JobOfferRequestDto;
import com.fstg.JobOfferManagement.dto.JobOfferResponseDto;
import com.fstg.JobOfferManagement.service.JobOfferService;

import jakarta.validation.Valid;

import java.nio.file.Files;
import java.io.File;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/JobOffers")
public class JobOfferController {
	
	private JobOfferService service ;
	
	public JobOfferController(JobOfferService service) {
		this.service= service ;
	}
	
	@GetMapping("/Offers")
	public ResponseEntity< List<JobOfferResponseDto> > getJobOffers(){
		//return service.findAll();	
		return new ResponseEntity<> (service.findAll(),HttpStatus.OK) ;
	}
	@PostMapping("/AddOffer")
	public ResponseEntity<JobOfferResponseDto> save(@ModelAttribute JobOfferRequestDto request) {
		JobOfferResponseDto dto = service.save(request) ;
		return new ResponseEntity <> (dto,HttpStatus.CREATED) ;
	}
	
	@PutMapping("/Offers/update/{id}")
    public ResponseEntity<JobOfferResponseDto> update(@Valid @RequestBody JobOfferRequestDto produitDto,@PathVariable Integer id) throws NotFoundException {
		JobOfferResponseDto dto = service.update(produitDto, id);
        return ResponseEntity.accepted().body(dto);
    }
	
	@GetMapping("/Offers/id/{id}")
	public ResponseEntity<JobOfferResponseDto> findById(@PathVariable Integer id) {
		JobOfferResponseDto dto = service.findById(id) ;
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/Offers/title/{title}")
	public JobOfferResponseDto findByTitle(@PathVariable String title) {
		return service.findByTitle(title);
	}
	
	
	/*@GetMapping("/Offers/recruiter/{id}")
	public JobOfferResponseDto findByRecruiter(@PathVariable Integer id) {
		return service.findByRecruiter(id);
	}
	*/
	@DeleteMapping("/Offers/id/{id}")
	public ResponseEntity<?> delet(@PathVariable Integer id) {
		service.delete(id);
        return ResponseEntity.noContent().build();
	}

	@GetMapping("/Offers/file/{id}")
	public ResponseEntity<byte[]> getJobOfferFileById(@PathVariable Integer id) throws IOException {
		// Path to the PDF file
		String filePath = "C:\\jobOffersDescriptions\\jobOffer_" + id + ".pdf";
		File file = new File(filePath);

		if (!file.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		try {
			// Read the PDF file content into a byte array
			byte[] fileContent = Files.readAllBytes(file.toPath());

			// Set the headers for PDF file download
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=jobOffer_" + id + ".pdf");
			headers.setContentType(MediaType.APPLICATION_PDF);

			// Return the PDF file content as byte[]
			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(file.length())
					.body(fileContent);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

}



	
}
