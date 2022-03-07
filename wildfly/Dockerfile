FROM quay.io/wildfly/wildfly:26.0.1.Final

# Create admin account, to log into WildFly
# --> insert command to create admin account here <--
# (see "Admin Account" @ README.md)

# Will be used as Env Var for Java Application
# --> insert Environment Variable here <--
# (see "Application Config" @ README.md)

# Contains simple Java App which returns Hello World at path /hello
# --> add application files to deployment directory here <--
# (see "Application Deployment" @ README.md)

# default command to start WildFly Application Server
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
