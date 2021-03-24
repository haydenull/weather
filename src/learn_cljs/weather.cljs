(ns ^:figwheel-hooks learn-cljs.weather
    (:require
     [goog.dom :as gdom]
     [reagent.dom :as rdom]
     [reagent.core :as r]))

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

;; 输入地区码
(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Enter your postal code"]
   [:input {:type "text"
            :placeholder "Postal Code"
            :value (:postal-code @app-state)}]
   [:button "Go"]])

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