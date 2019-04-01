package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.Product;
import com.es.phoneshop.model.product.exceptions.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class HttpSessionHistoryService implements HistoryService {
    protected static final String HTTP_SESSION_HISTORY_KEY = "httpHistory";
    protected final static int MAX_HISTORY_SIZE = 3;
    private static HttpSessionHistoryService INSTANCE;

    private HttpSessionHistoryService() {
    }

    public static HttpSessionHistoryService getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpSessionHistoryService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpSessionHistoryService();
                }
            }
        }
        return INSTANCE;
    }

    private void add(List<Product> historyProducts, Long productId) throws ProductNotFoundException {
        Product product = ArrayListProductDao.getInstance().getProduct(productId);
        int i;
        for (i = 0; i < historyProducts.size(); ++i) {
            if (historyProducts.get(i).getId().equals(productId)) {
                Product temp = historyProducts.get(i);
                historyProducts.set(i, historyProducts.get(0));
                historyProducts.set(0, temp);
                break;
            }
        }
        if (i == historyProducts.size()) {
            historyProducts.add(0, product);
        }
        if (i == MAX_HISTORY_SIZE) {
            historyProducts.remove(MAX_HISTORY_SIZE - 1);
        }
    }

    @Override
    public void update(HttpServletRequest req, Long productId) throws ProductNotFoundException {
        HttpSession session = req.getSession();
        if (session.getAttribute(HTTP_SESSION_HISTORY_KEY) == null) {
            List<Product> historyProducts = new ArrayList<>();
            session.setAttribute(HTTP_SESSION_HISTORY_KEY, historyProducts);
        }
        List<Product> historyProducts = (List<Product>) session.getAttribute(HTTP_SESSION_HISTORY_KEY);
        if (historyProducts != null) {
            if (productId != null) {
                add(historyProducts, productId);
            }
            req.setAttribute("history", historyProducts);
        }
    }
}