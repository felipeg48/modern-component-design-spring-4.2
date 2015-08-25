package demo.web;

import java.net.URI;
import java.net.URISyntaxException;

import demo.domain.BookNotFoundException;
import demo.domain.BookOrder;
import demo.order.BookOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BookController {

	private final BookOrderService bookOrderService;

	private final RepositoryEntityLinks entityLinks;

	@Autowired
	public BookController(BookOrderService bookOrderService, RepositoryEntityLinks entityLinks) {
		this.bookOrderService = bookOrderService;
		this.entityLinks = entityLinks;
	}

	@RequestMapping(path = "/books/order", method = RequestMethod.POST)
	public ResponseEntity<?> order(@RequestParam Long id) throws URISyntaxException {
		BookOrder order = this.bookOrderService.order(id, 42L);
		URI location = new URI(entityLinks.linkToSingleResource(BookOrder.class, order.getId()).getHref());
		return ResponseEntity.created(location).build();
	}

	@ExceptionHandler(BookNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Book not found")
	public void handleBookNotFoundException() {
	}

}