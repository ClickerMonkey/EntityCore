#ifndef ENTITYCORE_H
#define ENTITYCORE_H

#include <Common.h>
#include <EntityType.h>
#include <View.h>
#include <Controller.h>
#include <ComponentType.h>
#include <DynamicComponentType.h>
#include <IdMap.h>

class EntityCore 
{
public:
  
  static vector<EntityType*> getEntityTypes() 
  {
    return entityTypes;
  }

  inline static EntityType* getEntityType(const size_t entityTypeId) 
  {
    return entityTypes.at(entityTypeId);
  }

  inline static bool hasEntityType(const size_t entityTypeId) 
  {
    return (entityTypeId < entityTypes.size());
  }

  inline static EntityType* getEntityTypeSafe(const size_t entityTypeId) 
  {
    return hasEntityType(entityTypeId) ? getEntityType(entityTypeId) : nullptr;
  }

  static size_t newEntityType(IdMap components, IdMap controllers, size_t viewId);
  
  static vector<ComponentType*> getComponents() 
  {
    return componentTypes;
  }

  inline static ComponentType* getComponent(const size_t componentId) 
  {
    return componentTypes.at(componentId);
  }

  inline static bool hasComponent(const size_t componentId) 
  {
    return (componentId < componentTypes.size());
  }

  inline static ComponentType* getComponentSafe(const size_t componentId) 
  {
    return hasComponent(componentId) ? getComponent(componentId) : nullptr;
  }

  template<typename T>
  static size_t newComponent(const char *name, T defaultValue) 
  {
    size_t id = componentTypes.size();
    componentTypes.push_back(new ComponentType(id, name, AnyMemory(defaultValue)));
    return id;
  }

  template<typename T>
  static size_t newDynamicComponent(const char *name, DynamicComponent<T> *dynamicComponent)
  {
    size_t id = componentTypes.size();
    componentTypes.push_back(new DynamicComponentType<T>(id, name, dynamicComponent));
    return id;
  }

  template<typename T>
  static DynamicComponent<T>* getDynamicComponent(const size_t componentId)
  {
    if (!hasComponent(componentId))
    {
      return nullptr;
    }

    ComponentType *componentType = componentTypes.at(componentId);

    DynamicComponentType<T> *dynamicComponentType = dynamic_cast<DynamicComponentType<T>*>( componentType );

    if ( dynamicComponentType == nullptr )
    {
      return nullptr;
    }

    return dynamicComponentType->dynamicComponent;
  }

  static vector<View*> getViews() 
  {
    return views;
  }

  inline static View* getView(const size_t viewId) 
  {
    return views.at(viewId);
  }

  inline static bool hasView(const size_t viewId) 
  {
    return (viewId < views.size() && views.at(viewId) != nullptr);
  }

  inline static View* getViewSafe(const size_t viewId) 
  {
    return hasView(viewId) ? getView(viewId) : nullptr;
  }

  static size_t addView(View *view);

  static size_t newView() 
  {
    return addView(nullptr);
  }

  static void setView(const size_t viewId, View* view) 
  {
    views[viewId] = view;
  }

  static vector<Controller*> geControllers() 
  {
    return controllers;
  }

  inline static Controller* getController(const size_t controllerId) 
  {
    return controllers.at(controllerId);
  }

  inline static bool hasController(const size_t controllerId) 
  {
    return (controllerId < controllers.size());
  }

  inline static Controller* getControllerSafe(const size_t controllerId) 
  {
    return hasController(controllerId) ? getController(controllerId) : nullptr;
  }

  static size_t addController(Controller *controller);

private:

  static vector<ComponentType*> componentTypes;
  static vector<Controller*> controllers;
  static vector<View*> views;
  static vector<EntityType*> entityTypes;
  
};

#endif