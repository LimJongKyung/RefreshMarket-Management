package com.manage.product.controller;

import com.manage.customer.service.CustomerService;
import com.manage.menu.entity.Menu;
import com.manage.menu.service.MenuService;
import com.manage.product.entity.Product;
import com.manage.product.entity.ProductDetailImage;
import com.manage.product.entity.ProductImage;
import com.manage.product.repository.ProductDetailImageRepository;
import com.manage.product.repository.ProductImageRepository;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final MenuService menuService;
	private final CustomerService customerService;
	private final ProductImageRepository productImageRepository;
	private final ProductDetailImageRepository productDetailImageRepository;

	@Autowired
    public ProductController(ProductService productService, MenuService menuService
    		, CustomerService customerService, ProductImageRepository productImageRepository,
    		ProductDetailImageRepository productDetailImageRepository) {
        this.productService = productService;
        this.menuService = menuService;
        this.customerService = customerService;
        this.productImageRepository = productImageRepository;
        this.productDetailImageRepository = productDetailImageRepository;
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

        // 추가 이미지 조회
        List<ProductImage> extraImages = productService.getImagesByProductId(id);

        // 상세페이지 이미지 조회
        List<ProductDetailImage> detailImages = productService.getDetailImagesByProductId(id);

        model.addAttribute("product", product);
        model.addAttribute("extraImages", extraImages);
        model.addAttribute("detailImages", detailImages); // 새로 추가
        return "manage/product/detailP";  
    }
    
    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") Long id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "상품 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/product';</script>");
            response.getWriter().flush();
            return null;
        }

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        List<Menu> categories = menuService.getAllMenus();
        model.addAttribute("categories", categories);

        // 추가 이미지 조회
        List<ProductImage> extraImages = productService.getExtraImagesByProductId(id);
        model.addAttribute("extraImages", extraImages);

        // 상세페이지 이미지 조회
        List<ProductDetailImage> detailImages = productService.getDetailImagesByProductId(id);
        model.addAttribute("detailImages", detailImages);

        return "manage/product/modifyP";
    }

    @PostMapping("/update")
    public String updateProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "mainDisplay", required = false) String mainDisplay,
            @RequestParam(value = "detailOption", required = false) String detailOption,
            @RequestParam(value = "detailOptionPrice", required = false) String detailOptionPrice,
            @RequestParam(value = "extraImages", required = false) List<MultipartFile> extraImages,
            @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages,
            @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
            @RequestParam(value = "deleteDetailImageIds", required = false) List<Long> deleteDetailImageIds,  // 추가
            HttpSession session,
            HttpServletResponse response
    ) throws IOException {

        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "상품 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/product';</script>");
            response.getWriter().flush();
            return null;
        }

        try {
            if (mainDisplay == null) mainDisplay = "";
            if (detailOption == null) detailOption = "";
            if (detailOptionPrice == null) detailOptionPrice = "";

            productService.updateProduct(
                    product,
                    imageFile,
                    extraImages,
                    detailImages,
                    deleteImageIds,
                    deleteDetailImageIds,   // 삭제 ID 전달
                    mainDisplay,
                    detailOption,
                    detailOptionPrice
            );
        } catch (IllegalArgumentException e) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('" + e.getMessage() + "'); location.href='/product';</script>");
            response.getWriter().flush();
            return null;
        }

        return "redirect:/product";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();

        // DB 구분용 타입이 아니라 실제 MIME 타입을 사용
        String mimeType = product.getImageType();
        if (mimeType == null || mimeType.isBlank() || mimeType.equals("MAIN") || mimeType.equals("ADDITIONAL")) {
            mimeType = "image/jpeg"; // 기본값 지정
        }

        headers.setContentType(MediaType.parseMediaType(mimeType));
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/extraImage/{id}")
    public ResponseEntity<byte[]> getExtraImage(@PathVariable Long id) {
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null || productImage.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String mimeType = productImage.getImageType();
        if (mimeType == null || mimeType.isBlank() || mimeType.equals("MAIN") || mimeType.equals("ADDITIONAL")) {
            mimeType = "image/jpeg";
        }
        headers.setContentType(MediaType.parseMediaType(mimeType));

        return new ResponseEntity<>(productImage.getImage(), headers, HttpStatus.OK);
    }

    // 상세페이지 이미지
    @GetMapping("/detailImage/{id}")
    public ResponseEntity<byte[]> getDetailImage(@PathVariable Long id) {
        ProductDetailImage detailImage = productDetailImageRepository.findById(id).orElse(null);
        if (detailImage == null || detailImage.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String mimeType = detailImage.getImageType();
        if (mimeType == null || mimeType.isBlank() || mimeType.equals("DETAIL")) {
            mimeType = "image/jpeg"; // 기본값
        }
        headers.setContentType(MediaType.parseMediaType(mimeType));

        return new ResponseEntity<>(detailImage.getImage(), headers, HttpStatus.OK);
    }

    
    // 삭제 처리
    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id,
                                HttpSession session,
                                HttpServletResponse response) throws IOException {

        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "상품 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/product';</script>");
            response.getWriter().flush();
            return null;
        }

        productService.deleteProduct(id);
        return "redirect:/product";
    }
    
    @GetMapping("/new")
    public String showProductForm(Model model) {
    	List<Menu> categories = menuService.getAllMenus();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "manage/product/newP"; // new.html
    }

    @PostMapping("/new")
    public String createProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile mainImage,
            @RequestParam("mainImageType") String mainImageType,  
            @RequestParam(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @RequestParam(value = "additionalImageTypes", required = false) List<String> additionalImageTypes, 
            @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages,  // 상세페이지 이미지
            @RequestParam(value = "detailImageTypes", required = false) List<String> detailImageTypes,   // 상세페이지 이미지 타입
            HttpSession session,
            HttpServletResponse response,
            Model model) throws IOException {

        // 권한 체크
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/product';</script>");
            response.getWriter().flush();
            return null;
        }

        try {
            productService.saveProduct(
                product, 
                mainImage, mainImageType, 
                additionalImages, additionalImageTypes,
                detailImages, detailImageTypes  // 서비스 메서드에 상세 이미지 추가
            );
            return "redirect:/product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 저장 중 오류 발생");
            return "manage/product/newP";
        }
    }
}
