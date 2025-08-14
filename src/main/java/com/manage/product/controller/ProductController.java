package com.manage.product.controller;

import com.manage.menu.entity.Menu;
import com.manage.menu.service.MenuService;
import com.manage.product.entity.Product;
import com.manage.product.repository.ProductRepository;
import com.manage.product.service.ProductService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final MenuService menuService;
	private final ProductRepository productRepository;

	@Autowired
    public ProductController(ProductService productService, MenuService menuService, ProductRepository productRepository) {
        this.productService = productService;
        this.menuService = menuService;
        this.productRepository = productRepository;
    }
	
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String keyword,
                               Model model) {

        PageRequest pageable = PageRequest.of(page, 10);
        Page<Product> productPage = productService.getProductList(searchType, keyword, pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchTried", searchType != null || keyword != null);

        return "manage/product/list";  // templates/product/list.html
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "manage/product/detailP";  
    }
    
    
    // 수정 폼으로 이동
    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        
        List<Menu> categories = menuService.getAllMenus();
        model.addAttribute("categories", categories);
        return "manage/product/modifyP"; // templates/product/updateForm.html
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @RequestParam(value = "mainDisplay", required = false) String[] mainDisplays,
                                @RequestParam(value = "detailOption", required = false) String detailOption,
                                @RequestParam(value = "detailOptionPrice", required = false) String detailOptionPrice) throws IOException {

        // 이미지 파일 처리
        if (!imageFile.isEmpty()) {
            product.setImageData(imageFile.getBytes());
            product.setImageType(imageFile.getContentType());
        }

        // 메인 화면 표시 항목 처리
        if (mainDisplays != null && mainDisplays.length > 0) {
            String joined = String.join(",", mainDisplays);
            product.setMainDisplay(joined);
        } else {
            product.setMainDisplay(null);
        }

        // 상세 옵션 및 옵션별 추가 가격 설정
        product.setDetailOption(detailOption != null ? detailOption.trim() : null);
        product.setDetailOptionPrice(detailOptionPrice != null ? detailOptionPrice.trim() : null);

        productRepository.save(product);
        productService.updateProduct(product);
        return "redirect:/product";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String mimeType = product.getImageType();

        // MIME 타입이 없으면 기본값 지정 (예: JPEG)
        if (mimeType == null || mimeType.isBlank()) {
            mimeType = "image/jpeg"; // 또는 "application/octet-stream"
        }

        headers.setContentType(MediaType.parseMediaType(mimeType));
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }
    
    // 삭제 처리
    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/product"; // 삭제 후 목록 페이지로 이동
    }
    
    @GetMapping("/new")
    public String showProductForm(Model model) {
    	List<Menu> categories = menuService.getAllMenus();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "manage/product/newP"; // new.html
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute @Valid Product product,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @RequestParam(value = "mainDisplay", required = false) String[] mainDisplay,
                                @RequestParam(value = "detailOption", required = false) String detailOption,
                                @RequestParam(value = "detailOptionPrice", required = false) String detailOptionPrice,
                                Model model) {
        try {
            // 메인 화면 체크박스 처리
            if (mainDisplay != null && mainDisplay.length > 0) {
                String joined = String.join(",", mainDisplay);
                product.setMainDisplay(joined);
            } else {
                product.setMainDisplay(null);
            }

            // 상세 옵션 및 옵션별 추가 가격 설정
            product.setDetailOption(detailOption != null ? detailOption.trim() : null);
            product.setDetailOptionPrice(detailOptionPrice != null ? detailOptionPrice.trim() : null);

            productService.saveProduct(product, imageFile);
            return "redirect:/product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 저장 중 오류 발생");
            return "manage/product/newP";
        }
    }
}
