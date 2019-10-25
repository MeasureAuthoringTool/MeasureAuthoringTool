FROM busybox:latest
ADD target/MeasureAuthoringTool.war MeasureAuthoringTool.war
CMD "sh" "cp /MeasureAuthoringTool.war /app"