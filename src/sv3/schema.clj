(ns sv3.schema
  (:require
   [schema.core :as s]
   [clojure.core :refer [count]]
   [compojure.api.sweet :refer :all]
   [clojure.string :refer [blank?]]
   ))


;; Switch namespace to the current buffer C-c M-n
;; compile C-c C-k
;; more detail here: https://github.com/clojure-emacs/cider




;; Common schema (basic value types
  
(def NonEmptyString (describe (s/both s/Str (s/pred (complement blank?))) "Non-empty string"))

(defn string-fits?
  [s min-length max-length]
  (let [l (.length s)]
    (and
     (< l max-length)
     (> l min-length))))

(defn Text
  "Use with one argument to create schema with max length constraints
  with two arguments to create schema with min length contrain"
  ([min-length max-length]
   (describe
    (s/both s/Str (s/pred #(string-fits? % min-length max-length)))
    (str "String that is " min-length " to " max-length " chars long"))
   )
  ([length]
   (describe
    (s/both s/Str (s/pred #(string-fits? % 1 length)))
    (str "Non-empty string (max length " length ")"))))


(s/defschema Code (Text 1 40))
(s/defschema Url NonEmptyString)
(s/defschema Currency "Currency type" (s/enum :EUR :USD :RUB :GBP))
(s/defschema Money (describe (s/pair s/Num "amount" Currency "currency") "Monetary value with currency" ) )
(s/defschema ProductAttributes  (describe {s/Str s/Str} "A map of product attributes"))
(s/defschema ProductId s/Int)

(s/defschema Country (s/enum :AF :AX :AL :DZ :AS :AD :AO :AI :AQ :AG :AR :AM :AW :AU :AT :AZ :BS :BH :BD :BB :BY :BE :BZ :BJ :BM :BT :BO :BQ :BA :BW :BV :BR :IO :BN :BG :BF :BI :KH :CM :CA :CV :KY :CF :TD :CL :CN :CX :CC :CO :KM :CG :CD :CK :CR :CI :HR :CU :CW :CY :CZ :DK :DJ :DM :DO :EC :EG :SV :GQ :ER :EE :ET :FK :FO :FJ :FI :FR :GF :PF :TF :GA :GM :GE :DE :GH :GI :GR :GL :GD :GP :GU :GT :GG :GN :GW :GY :HT :HM :VA :HN :HK :HU :IS :IN :ID :IR :IQ :IE :IM :IL :IT :JM :JP :JE :JO :KZ :KE :KI :KP :KR :KW :KG :LA :LV :LB :LS :LR :LY :LI :LT :LU :MO :MK :MG :MW :MY :MV :ML :MT :MH :MQ :MR :MU :YT :MX :FM :MD :MC :MN :ME :MS :MA :MZ :MM :NA :NR :NP :NL :NC :NZ :NI :NE :NG :NU :NF :MP :NO :OM :PK :PW :PS :PA :PG :PY :PE :PH :PN :PL :PT :PR :QA :RE :RO :RU :RW :BL :SH :KN :LC :MF :PM :VC :WS :SM :ST :SA :SN :RS :SC :SL :SG :SX :SK :SI :SB :SO :ZA :GS :SS :ES :LK :SD :SR :SJ :SZ :SE :CH :SY :TW :TJ :TZ :TH :TL :TG :TK :TO :TT :TN :TR :TM :TC :TV :UG :UA :AE :GB :US :UM :UY :UZ :VU :VE :VN :VG :VI :WF :EH :YE :ZM :ZW))

;; Building up the domain


(s/defschema ProductInfo
  {
   :sku Code
   (s/optional-key :title) (Text 200)
   (s/optional-key :attributes) ProductAttributes 
   (s/optional-key :classificationId)  s/Int
   (s/optional-key :brandId) s/Int
   (s/optional-key :supplierId) s/Int
   (s/optional-key :note) (Text 8000)
   (s/optional-key :shortDescription) (Text 1000)
   (s/optional-key :longDescription) (Text 4000)
   (s/optional-key :pictures) [Url]
   (s/optional-key :cost) Money
   (s/optional-key :salePrice) Money
   (s/optional-key :retailPrice) Money
   (s/optional-key :minOrderQuantity) [(s/one s/Num "quantity") (s/optional (Text 4000) "note")]
   (s/optional-key :incrementalQuantuty) s/Num
   })

;; Online sale

(s/defschema ContactInfo
  {
   (s/optional-key :first-name) (Text 100)
   (s/optional-key :last-name) (Text 100)
   (s/optional-key :company) (Text 100)
   (s/optional-key :phone) (Text 50)
   (s/optional-key :email) (Text 200)
   })

(s/defschema ShippingAddress
  {
   :postal-code (Text 10)
   :country Country
   (s/optional-key :region) (Text 50)
   :city (Text 50)
   :address [(Text 200)]
   })
(s/defschema ShippingMethod (s/pair (Text 50) "Carrier" (Text 50) "Class"))

(s/defschema ItemPromotion
  {
   :code Code
   :item-discount Money
   :shipping-discount Money
   })

(s/defschema ItemCharges
  {
   })


(s/defschema GiftWrap
  {
   :cost Money
   :tax-cost Money
   :message (Text 1000)
   :level (Text 100)
   })
(s/defschema SaleItem
  {
   :sku Code
   :quantity s/Num
   :unit-price Money
   :promotions [ItemPromotion]
   :fulfilment Code

   ;; charges
   :shipping-cost Money
   :shipping-tax-cost Money
   :tax Money
   :vat-rate Money
   ;; sale source info
   :item-sale-source Code
   :sale-source-id Code
   :buyer-user-id Code
   :buyer-feedback-rating Code

   :gift-wrap GiftWrap
   })



(s/defschema SalePromotion
  {
   :code Code
   :order-discount Money
   :shipping-discount Money
   })

(s/defschema SaleChargeType (s/enum :shipping :shipping-insurance :sales-tax :gift-wrap :recycling-fee :vat-shipping :vat-gift-wrap))
(s/defschema SaleCharge
  {
   :type SaleChargeType 
   :amount Money
   })
(s/defschema SaleCart
  {
   :items [SaleItem]
   :checkout-source s/Str
   :promotions [SalePromotion]
   :charges [SaleCharge]
   })

(s/defschema SaleInfo
  {
   :address ShippingAddress
   :method ShippingMethod
   :contact ContactInfo
   
   :clientSaleId s/Str
   :sellerSaleId s/Str
   
   (s/optional-key :notes) [(Text 5000)]
   })
