package com.neusoft.bsp.business.seller.service;

import com.neusoft.bsp.business.po.Product;
import com.neusoft.bsp.admin.user.po.User;
import com.neusoft.bsp.business.vo.ProductVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ProductService {
    int insert(Product product);

    int update(Product product);

    int delete(int pk);

    Product getById(int pd);


    List<Product> getAll();

    List<Product> getAllByFilter(Map<String, Object> map);

    List<ProductVO> getProducts(User user);

    int addProduct(ProductVO productvo);

    int updateProduct(ProductVO productvo);

    int deleteProduct(Product product);

    int updateSts(Product product);

    List<ProductVO> getProductsOnShelf(User user);

    String uploadPicture(MultipartFile picture, HttpServletRequest request);

    ProductVO getProduct(Product product);
}
