(defproject toyrobot "0.1.0-SNAPSHOT"
  :description "toyrobot"
  :url ""
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :jvm-opts ["-XX:MaxPermSize=256m"]
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :source-paths ["src"]
  :main toyrobot
  :profiles {:dev {:plugins []
                   :dependencies []
                   :source-paths ["dev"]}})
