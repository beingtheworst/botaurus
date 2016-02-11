(ns sv3.schema
  (:require
   [schema.core :as s]
   [compojure.api.sweet :refer :all]
   [clojure.string :refer [blank?]]

   ))


;; Switch namespace to the current buffer C-c M-n
;; compile C-c C-k
;; more detail here: https://github.com/clojure-emacs/cider
;; Common schema
  
(def NonEmptyString (describe (s/both s/Str (s/pred (complement blank?))) "Non-empty string"))

(s/defschema Code NonEmptyString)
(s/defschema Url NonEmptyString)
(s/defschema Currency "Currency type" (s/enum :EUR :USD :RUB :GBP))
(s/defschema Money (describe (s/pair s/Num "amount" Currency "currency") "Monetary value with currency" ) )
(s/defschema ProductAttributes  (describe {s/Str s/Str} "A map of product attributes"))
(s/defschema ProductId s/Int)

(s/defschema Country (s/enum :AF :AX :AL :DZ :AS :AD :AO :AI :AQ :AG :AR :AM :AW :AU :AT :AZ :BS :BH :BD :BB :BY :BE :BZ :BJ :BM :BT :BO :BQ :BA :BW :BV :BR :IO :BN :BG :BF :BI :KH :CM :CA :CV :KY :CF :TD :CL :CN :CX :CC :CO :KM :CG :CD :CK :CR :CI :HR :CU :CW :CY :CZ :DK :DJ :DM :DO :EC :EG :SV :GQ :ER :EE :ET :FK :FO :FJ :FI :FR :GF :PF :TF :GA :GM :GE :DE :GH :GI :GR :GL :GD :GP :GU :GT :GG :GN :GW :GY :HT :HM :VA :HN :HK :HU :IS :IN :ID :IR :IQ :IE :IM :IL :IT :JM :JP :JE :JO :KZ :KE :KI :KP :KR :KW :KG :LA :LV :LB :LS :LR :LY :LI :LT :LU :MO :MK :MG :MW :MY :MV :ML :MT :MH :MQ :MR :MU :YT :MX :FM :MD :MC :MN :ME :MS :MA :MZ :MM :NA :NR :NP :NL :NC :NZ :NI :NE :NG :NU :NF :MP :NO :OM :PK :PW :PS :PA :PG :PY :PE :PH :PN :PL :PT :PR :QA :RE :RO :RU :RW :BL :SH :KN :LC :MF :PM :VC :WS :SM :ST :SA :SN :RS :SC :SL :SG :SX :SK :SI :SB :SO :ZA :GS :SS :ES :LK :SD :SR :SJ :SZ :SE :CH :SY :TW :TJ :TZ :TH :TL :TG :TK :TO :TT :TN :TR :TM :TC :TV :UG :UA :AE :GB :US :UM :UY :UZ :VU :VE :VN :VG :VI :WF :EH :YE :ZM :ZW))

(s/defschema ProductInfo
  {
   :sku Code
   :title s/Str

   (s/optional-key :attributes) ProductAttributes 
   (s/optional-key :classificationId)  s/Int
   (s/optional-key :brandId) s/Int
   (s/optional-key :supplierId) s/Int
   (s/optional-key :note) s/Str
   (s/optional-key :shortDescription) s/Str
   (s/optional-key :longDescription) s/Str
   (s/optional-key :pictures) [Url]
   (s/optional-key :cost) Money
   (s/optional-key :salePrice) Money
   (s/optional-key :retailPrice) Money
   (s/optional-key :minOrderQuantity) [(s/one s/Num "quantity") (s/optional s/Str "note")]
   (s/optional-key :incrementalQuantuty) s/Num
   })

;; Online sale

(s/defschema ContactInfo
  {
   (s/optional-key :first-name) s/Str
   (s/optional-key :last-name) s/Str

   (s/optional-key :company) s/Str
   (s/optional-key :phone) s/Str
   (s/optional-key :email) s/Str
   })

(s/defschema ShippingAddress
  {
   (s/optional-key :postal-code) s/Str
   (s/optional-key :country) Country
   (s/optional-key :region) s/Str
   (s/optional-key :city) s/Str
   (s/optional-key :address) [s/Str]
   })
(s/defschema ShippingMethod (s/pair s/Str "Carrier" s/Str "Class"))


(s/defschema SaleInfo
  {
   :address ShippingAddress
   :method ShippingMethod
   (s/optional-key :contact) ContactInfo
   (s/optional-key :notes) [s/Str]
                       })



