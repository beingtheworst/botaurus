(ns sv3.core
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [sv3.schema :refer :all]
            ))


(defapi app
  (ring.swagger.ui/swagger-ui)
  (compojure.api.swagger/swagger-docs
   {:info {:title "Sku"
           :description "Simple SkuVault prototype"}
    :tags [{:name "hello", :description "says hello in Finnish"}]})
  (context "/query" []
           :tags ["query"]
           
           
           (GET "/products" []
                :return [ProductInfo]
                :query-params [query :- String]
                :summary "search products"
                (ok {:message (str "Terve, " name)}))
           


           )
  (context "/command" []
           :tags ["command"]
           (POST "/product" []
                 :return ProductId
                 :body [info ProductInfo]
                 :summary "Creates a new product"
                 (ok info))
           (POST "/sale" []
                 :return SaleInfo
                 :body [info SaleInfo]
                 :summary "Currently echoes back the sale"
                 (ok info))
           )

        )
