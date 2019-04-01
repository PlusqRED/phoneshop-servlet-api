package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.HttpSessionCartService;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpSession httpSession;

    private ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getParameter(ProductListPageServlet.QUERY)).thenReturn(null);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(HttpSessionCartService.HTTP_SESSION_CART_KEY)).thenReturn(Cart.class);
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        String PATH = "/WEB-INF/pages/productList.jsp";
        verify(request, times(1)).getRequestDispatcher(PATH);
        verify(request).setAttribute(ProductListPageServlet.PRODUCTS, ArrayListProductDao.getInstance().findProducts());
        verify(requestDispatcher).forward(request, response);
    }
}
