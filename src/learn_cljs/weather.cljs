(ns ^:figwheel-hooks learn-cljs.weather
    (:require
     [goog.dom :as gdom]
     [reagent.dom :as rdom]
     [reagent.core :as r]
     [ajax.core :as ajax]))

(def api-key "c7476bd248c8ae74e33e3cc188603f3d")

(defonce app-state (r/atom {:title "WhichWeather"
                            :postal-code ""
                            :temperatures {:today {:label "Today"
                                                   :value nil}
                                           :tomorrow {:label "Tomorrow"
                                                      :value nil}}}))
;; 标题
(defn title []
  [:h1 (:title @app-state)])

;; 单个温度组件
(defn temperature [temp]
  [:div {:class "temperature"}
   [:div {:class "value"}
    (:value temp)]
   [:h2 (:label temp)]])

(defn handle-response [resp]
  (let [today (get-in resp ["list" 0 "main" "temp"])
        tomorrow (get-in resp ["list" 0 "main" "temp"])]
    (swap! app-state
           update-in [:temperatures :today :value] (constantly today))
    (swap! app-state
           update-in [:temperatures :tomorrow :value] (constantly tomorrow))))

(defn get-forecast! []
  (let [postal-code (:postal-code @app-state)]
    (ajax/GET "http://api.openweathermap.org/data/2.5/forecast"
              {:params {"q" postal-code
                        "appid" api-key
                        "units" "imperial"}
               :handler handle-response})))
;; 输入地区码
(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Enter your postal code"]
   [:input {:type "text"
            :placeholder "Postal Code"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state assoc :postal-code (-> % .-target .-value))}]
   [:button {:on-click get-forecast!} "Go"]])


(defn app []
  [:div {:class "app"}
   [title]
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]
      [temperature temp])]
   [postal-code]])





;; (defn hello-world []
;;   [:div
;;    [:h1 {:class "app-title"} "Hello, World"]])

(defn mount-app-element []
  (rdom/render [app] (gdom/getElement "app")))
(mount-app-element)

(defn ^:after-load on-reload []
  (mount-app-element))