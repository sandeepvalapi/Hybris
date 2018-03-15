#!/bin/sh

SCRIPT_NAME=$0
PLATFORM_HOME=`pwd`
HYBRIS_ROLES_DIR="$PLATFORM_HOME/../../roles"
ACTIVE_ROLE_PROPS="$PLATFORM_HOME/active-role-env.properties"

print_help() {
cat << EOF
Usage: roles.sh [options...]

Create role:

-c, --create-role roleName          creates role named by roleName.
-C, --create-instance intanceName   creates role instance named by instanceName. This option requires specific tcp ports configuration:
    --role-name=roleName            name of the role for which instance will be created (mandatory)
    --http-port=portNumber          tomcat http port for newly created role (mandatory)
    --ssl-port=portNumber           tomcat ssl port for newly created role (mandatory)
    --ajp-port=portNumber           ajp port for newly created role (mandatory)
    --jmx-port=portNumber           jmx port for newly created role (mandatory)
    --jmx-server-port=portNumber    jmx server port for newly created role (mandatory)

Other options:

-a, --activate roleName                 activates role named by roleName
-A, --activate-instance instanceName    activates role instance named by instanceName. This option requires additional option:
    --role-name=roleName                name of the role for which instance must be activated
-d, --deactivate                        deactivates current active role or instance
-r, --remove roleName                   removes role roleName (*warning* it will remove whole role and all instances)
-R, --remove-instance instanceName      removes instance (*warning* instance must not be running). This option requires additional option:
    --role-name=roleName                name of the role for which instance must be removed
-l, --list                              list installed roles (the role marked with * is current active role)
-h, --help                              print help
EOF
    exit 0;
}

if [ -a ${ACTIVE_ROLE_PROPS} ]; then
    . "$ACTIVE_ROLE_PROPS"
fi

read_role_names() {
    ROLES_LIST="Role Instance Active Status PID\n----- --------- ------- ------- ----\n"

    for dir in ${HYBRIS_ROLES_DIR}/*
    do
        if [ -d ${dir} ]; then
            ROLE_NAME=`basename ${dir}`
            ACTIVE=

            if [ "$ACTIVE_ROLE" == `basename ${dir}` ]; then
                ACTIVE='A'
            else
                ACTIVE='-'
            fi

            ROLES_LIST+="$ROLE_NAME - $ACTIVE - -\n"

            for instDir in ${dir}/*
            do
                INSTANCE_NAME=`basename ${instDir}`
                RUNNING=

                if [ "base" != ${INSTANCE_NAME} ]; then
                    if [ "$ACTIVE_ROLE_INSTANCE" == `basename ${instDir}` ]; then
                        ACTIVE_INST='A'
                    else
                        ACTIVE_INST='-'
                    fi

                    INSTANCE_TOMCAT_PID_FILE="$HYBRIS_ROLES_DIR/$ROLE_NAME/$INSTANCE_NAME/tomcat/bin/hybrisPlatform.pid"

                    if [ -a ${INSTANCE_TOMCAT_PID_FILE} ]; then
                        PID=`cat ${INSTANCE_TOMCAT_PID_FILE}`
                        TOMCATPROCESS=$(ps -A| grep -v grep | grep ${PID} | head -1)

                        if [ -z "$TOMCATPROCESS" ]; then
                            RUNNING="not-running $INSTANCE_TOMCAT_PID_FILE"
                        else
                            RUNNING="running $PID"
                        fi
                    fi

                    ROLES_LIST+="+--> $INSTANCE_NAME $ACTIVE_INST $RUNNING\n"
                fi
            done
        fi
    done

    if [ -n "$ROLES_LIST" ]; then
        echo "Installed roles:\n"
        echo "$ROLES_LIST" |column -t 2>&1
    else
        echo "There are no roles installed in the system. Skipping" 2>&1
    fi
}

if [ ! "$1" ]; then
    print_help
fi

while :; do
    case $1 in
        -h|-\?|--help)
            print_help
            ;;
        -r|--remove)
            if [ "$2" ]; then
                if [ -d "$HYBRIS_ROLES_DIR/$2" ]; then
                    echo "Removing $HYBRIS_ROLES_DIR/$2 ...."
                    rm -rf "$HYBRIS_ROLES_DIR/$2"
                    if [ -a "$ACTIVE_ROLE_PROPS" ]; then
                        echo "Removing $ACTIVE_ROLE_PROPS file ...."
                        rm -rf "$ACTIVE_ROLE_PROPS"
                    fi
                else
                    echo "Role $2 not present in the system. Skipping" 2>&1
                fi
                exit 0
            else
                echo "ERROR: Must specify a non-empty role name" 2>&1
                exit 1
            fi
            ;;
        -R|--remove-instance)
            if [ "$2" ]; then
                remove_instance=1
                instance_name=$2
                shift 2
                continue
            else
                echo "ERROR: Must specify a non-empty instance name" 2>&1
                exit 1
            fi
            ;;
        -d|--deactivate)
            if [ -a "$ACTIVE_ROLE_PROPS" ]; then
                echo "Deactivating current active role"
                rm -rf ${PLATFORM_HOME}/active-role-env.properties
            else
                echo "None of the roles were active at the moment. Skipping" 2>&1
            fi
            exit 0
            ;;
        -l|--list)
            read_role_names
            exit 0
            ;;
        -a|--activate)
            if [ "$2" ]; then
                role_name=$2
                if [ -d "$HYBRIS_ROLES_DIR/$2" ]; then
                    . "$PLATFORM_HOME/setantenv.sh"
                    ant activateRole -Drole.name=${role_name}
                    exit 0
                else
                    echo "Role $2 not present in the system. Skipping" 2>&1
                    exit 1
                fi
            else
                echo 'ERROR: Must specify a non-empty role name' 2>&1
                exit 1
            fi
            ;;
        -A|--activate-instance)
            if [ "$2" ]; then
                activate_instance=1
                instance_name=$2
                shift 2
                continue
            else
                echo 'ERROR: Must specify a non-empty instance name' 2>&1
                exit 1
            fi
            ;;
        -c|--create-role)
            role_name=$2
            echo "Creating role '$role_name'" 2>&1
            . "$PLATFORM_HOME/setantenv.sh"
            ant createRole -Drole.name=${role_name}
            exit 0
            ;;
        -C|--create-instance)
            if [ "$2" ]; then
                create_instance=1
                instance_name=$2
                shift 2
                continue
            else
                echo 'ERROR: Must specify a non-empty instance name' 2>&1
                exit 1
            fi
            ;;
        --role-name=?*)
            role_name=${1#*=}
            shift 1
            continue
            ;;
        --http-port=?*)
            http_port=${1#*=}
            shift 1
            continue
            ;;
        --ssl-port=?*)
            ssl_port=${1#*=}
            shift 1
            continue
            ;;
        --ajp-port=?*)
            ajp_port=${1#*=}
            shift 1
            continue
            ;;
        --jmx-port=?*)
            jmx_port=${1#*=}
            shift 1
            continue
            ;;
        --jmx-server-port=?*)
            jmx_server_port=${1#*=}
            shift 1
            continue
            ;;
        *)
            break
    esac

    shift
done


if [ "$create_instance" ]; then
    if [ ! "$role_name" ] || [ ! "$http_port" ] || [ ! "$ssl_port" ] || [ ! "$ajp_port" ] || [ ! "$jmx_port" ] || [ ! "$jmx_server_port" ]; then
        echo "ERROR: options --role-name --http-port --ssl-port --ajp-port --jmx-port --jmx-server-port not given. See --help" 2>&1
        exit 1
    fi

    echo "Creating role instance '$instance_name'" 2>&1
    . "$PLATFORM_HOME/setantenv.sh"
    ant createInstance -Drole.name=${role_name} -Dinstance.name=${instance_name} -Dhttp.port=${http_port} -Dssl.port=${ssl_port} -Dajp.port=${ajp_port} -Djmx.port=${jmx_port} -Djmx.server.port=${jmx_server_port}
    exit 0
fi

if [ "$activate_instance" ]; then
    if [ ! "$role_name" ]; then
        echo "ERROR: option --role-name not given. See --help" 2>&1
        exit 1
    fi

    echo "Activating role instance '$instance_name'" 2>&1
    . "$PLATFORM_HOME/setantenv.sh"
    ant activateInstance -Drole.name=${role_name} -Dinstance.name=${instance_name}
fi

if [ "$remove_instance" ]; then
    if [ ! "$role_name" ]; then
        echo "ERROR: option --role-name not given. See --help" 2>&1
        exit 1
    fi

    if [ -d "$HYBRIS_ROLES_DIR/$role_name/$instance_name" ]; then
        INSTANCE_TOMCAT_PID_FILE="$HYBRIS_ROLES_DIR/$role_name/$instance_name/tomcat/bin/hybrisPlatform.pid"

        if [ -a ${INSTANCE_TOMCAT_PID_FILE} ]; then
            echo "ERROR: Cannot remove instance ${instance_name} while it is still running" 2>&1
            exit 1
        fi

        echo "Removing $HYBRIS_ROLES_DIR/$role_name/$instance_name ...."
        rm -rf "$HYBRIS_ROLES_DIR/$role_name/$instance_name"
        if [ -a "$ACTIVE_ROLE_PROPS" ] && [ "$ACTIVE_ROLE_INSTANCE" == ${instance_name} ]; then
            echo "Removing $ACTIVE_ROLE_PROPS file ...."
            rm -rf "$ACTIVE_ROLE_PROPS"
        fi
    else
        echo "Role $2 not present in the system. Skipping" 2>&1
    fi
    exit 0
    echo "Removing role instance '$instance_name'" 2>&1

fi



